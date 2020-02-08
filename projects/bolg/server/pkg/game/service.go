package game

import (
	"context"
	"errors"
	"fmt"
	"io"

	"github.com/bolg-developers/x/projects/bolg/server/pb"
	"go.uber.org/zap"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type Service struct {
	RoomDB RoomDB
	Logger *zap.Logger
}

func (svc *Service) Connect(stream pb.BolgService_ConnectServer) error {
	for {
		in, err := stream.Recv()
		if err == io.EOF {
			return nil
		}
		if err != nil {
			return err
		}
		switch msg := in.Data.(type) {
		case *pb.RoomMessage_CreateAndJoinRoomReq:
			svc.Logger.Info(fmt.Sprintf("Receive CreateAndJoinRoomReq: %+v", msg.CreateAndJoinRoomReq))
			if err := svc.handleCreateAndJoinRoomReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_JoinRoomReq:
			svc.Logger.Info(fmt.Sprintf("Receive JoinRoomReq: %+v", msg.JoinRoomReq))
			if err := svc.handleJoinRoomReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_UpdateWeaponReq:
			svc.Logger.Info(fmt.Sprintf("Receive UpdateWeaponReq: %+v", msg.UpdateWeaponReq))
			if err := svc.handleUpdateWeaponReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_ReadyReq:
			svc.Logger.Info(fmt.Sprintf("Receive ReadyReq: %+v", msg.ReadyReq))
			if err := svc.handleReadyReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_StartGameReq:
			svc.Logger.Info(fmt.Sprintf("Receive StartGameReq: %+v", msg.StartGameReq))
			if err := svc.handleStartGameReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_NotifyReceivingReq:
			svc.Logger.Info(fmt.Sprintf("Receive NotifyReceivingReq: %+v", msg.NotifyReceivingReq))
			if err := svc.handleNotifyReceivingReq(stream, msg); err != nil {
				return err
			}
		default:
			return status.Error(codes.InvalidArgument, "invalid room message")
		}
	}
}

//------------------------------------------------------------------------------
// Room Message Handlers
//------------------------------------------------------------------------------
func (svc *Service) handleCreateAndJoinRoomReq(
	stream pb.BolgService_ConnectServer,
	in *pb.RoomMessage_CreateAndJoinRoomReq,
) error {
	// 認証処理を実装するまでこれで
	// 認証処理はConnect関数で実行して以下は削除する
	pname := in.CreateAndJoinRoomReq.PlayerName
	if pname == "" {
		return svc.sendRoomMessageError(stream, codes.InvalidArgument, "player name is empty")
	}

	r := NewRoom()

	p := NewPlayer(pname)
	p.Stream = stream
	if err := r.AddPlayer(p); err != nil {
		return svc.sendRoomMessageError(stream, codes.ResourceExhausted, "max player")
	}

	r.OwnerID = p.ID

	if err := svc.RoomDB.Create(r); err != nil {
		msg := fmt.Sprintf("Error handleCreateAndJoinRoomReq: %s", err.Error())
		return svc.internalError(msg)
	}

	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_CreateAndJoinRoomResp{
			CreateAndJoinRoomResp: &pb.CreateAndJoinRoomResponse{
				Room:  r.ToProtoRoom(),
				Token: CreateToken(r.ID, p.ID),
			},
		},
	}
	if err := stream.Send(out); err != nil {
		return err
	}

	return nil
}

func (svc *Service) handleJoinRoomReq(
	stream pb.BolgService_ConnectServer,
	in *pb.RoomMessage_JoinRoomReq,
) error {
	// 認証処理を実装するまでこれで
	// 認証処理はConnect関数で実行して以下は削除する
	pname := in.JoinRoomReq.PlayerName
	if pname == "" {
		return svc.sendRoomMessageError(stream, codes.InvalidArgument, "player name is empty")
	}

	r, err := svc.RoomDB.Get(in.JoinRoomReq.RoomId)
	if errors.Is(err, ErrRoomDBNotFound) {
		return svc.sendRoomMessageError(stream, codes.NotFound, fmt.Sprintf("not found room with ID %d", in.JoinRoomReq.RoomId))
	}

	if r.GameStart {
		return svc.sendRoomMessageError(stream, codes.FailedPrecondition, "game is starting")
	}

	p := NewPlayer(pname)
	p.Stream = stream
	if err := r.AddPlayer(p); err != nil {
		return svc.sendRoomMessageError(stream, codes.ResourceExhausted, "max player")
	}

	if err := svc.RoomDB.Update(r); err != nil {
		return svc.internalError(fmt.Sprintf("Error handleUpdateWeaponReq roomdb.Update: %s", err.Error()))
	}

	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_JoinRoomResp{
			JoinRoomResp: &pb.JoinRoomResponse{
				Room:  r.ToProtoRoom(),
				Token: CreateToken(r.ID, p.ID),
			},
		},
	}
	if err := stream.Send(out); err != nil {
		return err
	}

	out = &pb.RoomMessage{
		Data: &pb.RoomMessage_JoinRoomMsg{
			JoinRoomMsg: &pb.JoinRoomMessage{
				Player: p.ToProtoPlayer(),
			},
		},
	}
	if errs := r.Broadcast(FilterNotByPlayerID(p.ID), out); len(errs) != 0 {
		svc.Logger.Error(fmt.Sprintf("broadcast error: %+v", errs))
	}

	return nil
}

func (svc *Service) handleUpdateWeaponReq(
	stream pb.BolgService_ConnectServer,
	in *pb.RoomMessage_UpdateWeaponReq,
) error {
	rid, pid, err := ParseFromToken(in.UpdateWeaponReq.Token)
	if errors.Is(err, ErrInvalidToken) {
		return status.Error(codes.Unauthenticated, "token error")
	}

	r, err := svc.RoomDB.Get(rid)
	if err != nil {
		if errors.Is(err, ErrRoomDBNotFound) {
			return status.Error(codes.Unauthenticated, "invalid token")
		}
		return svc.internalError(fmt.Sprintf("Error handleUpdateWeaponReq roomdb.Get: %s", err.Error()))
	}

	if r.GameStart {
		return svc.sendRoomMessageError(stream, codes.FailedPrecondition, "game is starting")
	}

	p, err := r.GetPlayer(pid)
	if err != nil {
		return status.Error(codes.Unauthenticated, "token error")
	}
	p.Power = in.UpdateWeaponReq.Attack
	if err := r.UpdatePlayer(p); err != nil {
		return svc.internalError(fmt.Sprintf("Error handleUpdateWeaponReq roomdb.UpdatePlayer: %s", err.Error()))
	}

	if err := svc.RoomDB.Update(r); err != nil {
		return svc.internalError(fmt.Sprintf("Error handleUpdateWeaponReq roomdb.Update: %s", err.Error()))
	}

	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_UpdateWeaponResp{
			UpdateWeaponResp: &pb.UpdateWeaponResponse{},
		},
	}
	if err := stream.Send(out); err != nil {
		return err
	}

	return nil
}

func (svc *Service) handleReadyReq(
	stream pb.BolgService_ConnectServer,
	in *pb.RoomMessage_ReadyReq,
) error {
	rid, pid, err := ParseFromToken(in.ReadyReq.Token)
	if errors.Is(err, ErrInvalidToken) {
		return status.Error(codes.Unauthenticated, "token error")
	}

	r, err := svc.RoomDB.Get(rid)
	if err != nil {
		if errors.Is(err, ErrRoomDBNotFound) {
			return status.Error(codes.Unauthenticated, "invalid token")
		}
		return svc.internalError(fmt.Sprintf("Error handleReadyReq roomdb.Get: %s", err.Error()))
	}

	p, err := r.GetPlayer(pid)
	if err != nil {
		return status.Error(codes.Unauthenticated, "token error")
	}
	if p.Ready {
		return nil
	}
	p.Ready = true
	if err := r.UpdatePlayer(p); err != nil {
		return svc.internalError(fmt.Sprintf("Error handleReadyReq roomdb.UpdatePlayer: %s", err.Error()))
	}

	if err := svc.RoomDB.Update(r); err != nil {
		return svc.internalError(fmt.Sprintf("Error handleReadyReq roomdb.Update: %s", err.Error()))
	}

	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_ReadyMsg{
			ReadyMsg: &pb.ReadyMessage{
				PlayerId: p.ID,
			},
		},
	}
	if errs := r.Broadcast(FilterNothing(), out); len(errs) != 0 {
		svc.Logger.Error(fmt.Sprintf("broadcast error: %+v", errs))
	}

	return nil
}

func (svc *Service) handleStartGameReq(
	stream pb.BolgService_ConnectServer,
	in *pb.RoomMessage_StartGameReq,
) error {
	rid, pid, err := ParseFromToken(in.StartGameReq.Token)
	if errors.Is(err, ErrInvalidToken) {
		return status.Error(codes.Unauthenticated, "token error")
	}

	r, err := svc.RoomDB.Get(rid)
	if err != nil {
		if errors.Is(err, ErrRoomDBNotFound) {
			return status.Error(codes.Unauthenticated, "invalid token")
		}
		return svc.internalError(fmt.Sprintf("Error handleStartGameReq roomdb.Get: %s", err.Error()))
	}

	if r.OwnerID != pid {
		return svc.sendRoomMessageError(stream, codes.PermissionDenied, "you are not owner")
	}

	if r.GameStart {
		return nil
	}

	// 準備完了じゃないプレイヤーがいたらできない。
	for _, p := range r.Players {
		if !p.Ready {
			return svc.sendRoomMessageError(stream, codes.FailedPrecondition, "some players are not ready yet")
		}
	}

	r.GameStart = true
	r.ResetHP()

	if err := svc.RoomDB.Update(r); err != nil {
		return svc.internalError(fmt.Sprintf("Error handleStartGameReq roomdb.Update: %s", err.Error()))
	}

	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_StartGameMsg{
			StartGameMsg: &pb.StartGameMessage{
				Room: r.ToProtoRoom(),
			},
		},
	}
	if errs := r.Broadcast(FilterNothing(), out); len(errs) != 0 {
		svc.Logger.Error(fmt.Sprintf("broadcast error: %+v", errs))
	}

	return nil
}

func (svc *Service) handleNotifyReceivingReq(
	stream pb.BolgService_ConnectServer,
	in *pb.RoomMessage_NotifyReceivingReq,
) error {
	rid, pid, err := ParseFromToken(in.NotifyReceivingReq.Token)
	if errors.Is(err, ErrInvalidToken) {
		return status.Error(codes.Unauthenticated, "token error")
	}

	r, err := svc.RoomDB.Get(rid)
	if err != nil {
		if errors.Is(err, ErrRoomDBNotFound) {
			return status.Error(codes.Unauthenticated, "invalid token")
		}
		return svc.internalError(fmt.Sprintf("Error handleStartGameReq roomdb.Get: %s", err.Error()))
	}

	receiver, err := r.GetPlayer(pid)
	if err != nil {
		return status.Error(codes.Unauthenticated, "token error")
	}
	sender, err := r.GetPlayer(in.NotifyReceivingReq.PlayerId)
	if err != nil {
		return svc.sendRoomMessageError(
			stream,
			codes.FailedPrecondition,
			fmt.Sprintf("not found sender with ID %d", in.NotifyReceivingReq.PlayerId),
		)
	}

	var killerName string
	receiver.Hp = Damage(sender.Power, receiver.Hp)
	if IsDead(receiver.Hp) {
		sender.KillCnt++
		if err := r.UpdatePlayer(sender); err != nil {
			return svc.internalError(fmt.Sprintf("Error handleNotifyReceivingReq roomdb.UpdatePlayer(sender): %s", err.Error()))
		}
		killerName = sender.Name
	}
	if err := r.UpdatePlayer(receiver); err != nil {
		return svc.internalError(fmt.Sprintf("Error handleNotifyReceivingReq roomdb.UpdatePlayer(receiver): %s", err.Error()))
	}

	// ヒット&死亡通知
	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_NotifyReceivingMsg{
			NotifyReceivingMsg: &pb.NotifyReceivingMessage{
				Player:     receiver.ToProtoPlayer(),
				KillerName: killerName,
			},
		},
	}
	if errs := r.Broadcast(FilterNothing(), out); len(errs) != 0 {
		svc.Logger.Error(fmt.Sprintf("broadcast error(NotifyReceivingMsg): %+v", errs))
	}

	winner, done := SurvivalJudge(r.Players)
	if !done {
		return nil
	}

	// ゲームリザルト通知
	out = &pb.RoomMessage{
		Data: &pb.RoomMessage_SurvivalResultMsg{
			SurvivalResultMsg: &pb.SurvivalResultMessage{
				Winner:    winner.ToProtoPlayer(),
				Personals: CreateSurvivalPersonalResults(r.Players),
			},
		},
	}
	if errs := r.Broadcast(FilterNothing(), out); len(errs) != 0 {
		svc.Logger.Error(fmt.Sprintf("broadcast error(NotifyReceivingMsg): %+v", errs))
	}

	r.GameStart = false
	r.InitPlayers()
	if err := svc.RoomDB.Update(r); err != nil {
		return svc.internalError(fmt.Sprintf("Error handleNotifyReceivingReq roomdb.Update: %s", err.Error()))
	}

	return nil
}

//------------------------------------------------------------------------------
// Normal RPCs
//------------------------------------------------------------------------------
func (svc *Service) CheckHealth(_ context.Context, in *pb.CheckHealthRequest) (*pb.CheckHealthResponse, error) {
	out := &pb.CheckHealthResponse{Message: fmt.Sprintf("Hello, %s! I'm fine :)", in.Name)}
	return out, nil
}

// TODO: 実装する
func (svc *Service) GetStamina(_ context.Context, in *pb.GetStaminaRequest) (*pb.Stamina, error) {
	return &pb.Stamina{}, nil
}

// TODO: 実装する
func (svc *Service) UseStamina(_ context.Context, in *pb.UseStaminaRequest) (*pb.UseStaminaResponse, error) {
	return &pb.UseStaminaResponse{}, nil
}

// TODO: 実装する
func (svc *Service) RecoverStamina(_ context.Context, in *pb.RecoverStaminaRequest) (*pb.RecoverStaminaResponse, error) {
	return &pb.RecoverStaminaResponse{}, nil
}

//------------------------------------------------------------------------------
// Helpers
//------------------------------------------------------------------------------
func (svc *Service) internalError(msg string) error {
	svc.Logger.Error(msg)
	return status.Error(codes.Internal, "unknown error")
}

func (svc *Service) sendRoomMessageError(stream pb.BolgService_ConnectServer, code codes.Code, msg string) error {
	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_Error{
			Error: &pb.Error{
				Code:    int32(code),
				Message: msg,
			},
		},
	}
	return stream.Send(out)
}
