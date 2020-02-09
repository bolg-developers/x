package main

import (
	"context"
	"errors"
	"fmt"
	"io"
	"net"

	"github.com/bolg-developers/x/projects/bolg/server/pb"
	"github.com/bolg-developers/x/projects/bolg/server/pkg/account"
	"github.com/bolg-developers/x/projects/bolg/server/pkg/game"
	"github.com/bolg-developers/x/projects/bolg/server/pkg/kakin"
	"go.uber.org/zap"
	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type Service struct {
	roomDB    game.RoomDB
	accountDB account.AccountDB
	staminaDB kakin.StaminaDB
	logger    *zap.Logger
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
			svc.logger.Info(fmt.Sprintf("Receive CreateAndJoinRoomReq: %+v", msg.CreateAndJoinRoomReq))
			if err := svc.handleCreateAndJoinRoomReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_JoinRoomReq:
			svc.logger.Info(fmt.Sprintf("Receive JoinRoomReq: %+v", msg.JoinRoomReq))
			if err := svc.handleJoinRoomReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_UpdateWeaponReq:
			svc.logger.Info(fmt.Sprintf("Receive UpdateWeaponReq: %+v", msg.UpdateWeaponReq))
			if err := svc.handleUpdateWeaponReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_ReadyReq:
			svc.logger.Info(fmt.Sprintf("Receive ReadyReq: %+v", msg.ReadyReq))
			if err := svc.handleReadyReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_StartGameReq:
			svc.logger.Info(fmt.Sprintf("Receive StartGameReq: %+v", msg.StartGameReq))
			if err := svc.handleStartGameReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_NotifyReceivingReq:
			svc.logger.Info(fmt.Sprintf("Receive NotifyReceivingReq: %+v", msg.NotifyReceivingReq))
			if err := svc.handleNotifyReceivingReq(stream, msg); err != nil {
				return err
			}
		default:
			return status.Error(codes.InvalidArgument, "invalid room message")
		}
	}
}

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

	r := game.NewRoom()

	p := game.NewPlayer(pname)
	p.Stream = stream
	if err := r.AddPlayer(p); err != nil {
		return svc.sendRoomMessageError(stream, codes.ResourceExhausted, "max player")
	}

	r.OwnerID = p.ID

	if err := svc.roomDB.Create(r); err != nil {
		msg := fmt.Sprintf("Error handleCreateAndJoinRoomReq: %s", err.Error())
		return svc.internalError(msg)
	}

	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_CreateAndJoinRoomResp{
			CreateAndJoinRoomResp: &pb.CreateAndJoinRoomResponse{
				Room:  r.ToProtoRoom(),
				Token: game.CreateToken(r.ID, p.ID),
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

	r, err := svc.roomDB.Get(in.JoinRoomReq.RoomId)
	if errors.Is(err, game.ErrRoomDBNotFound) {
		return svc.sendRoomMessageError(stream, codes.NotFound, fmt.Sprintf("not found room with ID %d", in.JoinRoomReq.RoomId))
	}

	if r.GameStart {
		return svc.sendRoomMessageError(stream, codes.FailedPrecondition, "game is starting")
	}

	p := game.NewPlayer(pname)
	p.Stream = stream
	if err := r.AddPlayer(p); err != nil {
		return svc.sendRoomMessageError(stream, codes.ResourceExhausted, "max player")
	}

	if err := svc.roomDB.Update(r); err != nil {
		return svc.internalError(fmt.Sprintf("Error handleUpdateWeaponReq roomdb.Update: %s", err.Error()))
	}

	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_JoinRoomResp{
			JoinRoomResp: &pb.JoinRoomResponse{
				Room:  r.ToProtoRoom(),
				Token: game.CreateToken(r.ID, p.ID),
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
	if errs := r.Broadcast(game.FilterNotByPlayerID(p.ID), out); len(errs) != 0 {
		svc.logger.Error(fmt.Sprintf("broadcast error: %+v", errs))
	}

	return nil
}

func (svc *Service) handleUpdateWeaponReq(
	stream pb.BolgService_ConnectServer,
	in *pb.RoomMessage_UpdateWeaponReq,
) error {
	rid, pid, err := game.ParseFromToken(in.UpdateWeaponReq.Token)
	if errors.Is(err, game.ErrInvalidToken) {
		return status.Error(codes.Unauthenticated, "token error")
	}

	r, err := svc.roomDB.Get(rid)
	if err != nil {
		if errors.Is(err, game.ErrRoomDBNotFound) {
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

	if err := svc.roomDB.Update(r); err != nil {
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
	rid, pid, err := game.ParseFromToken(in.ReadyReq.Token)
	if errors.Is(err, game.ErrInvalidToken) {
		return status.Error(codes.Unauthenticated, "token error")
	}

	r, err := svc.roomDB.Get(rid)
	if err != nil {
		if errors.Is(err, game.ErrRoomDBNotFound) {
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

	if err := svc.roomDB.Update(r); err != nil {
		return svc.internalError(fmt.Sprintf("Error handleReadyReq roomdb.Update: %s", err.Error()))
	}

	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_ReadyMsg{
			ReadyMsg: &pb.ReadyMessage{
				PlayerId: p.ID,
			},
		},
	}
	if errs := r.Broadcast(game.FilterNothing(), out); len(errs) != 0 {
		svc.logger.Error(fmt.Sprintf("broadcast error: %+v", errs))
	}

	return nil
}

func (svc *Service) handleStartGameReq(
	stream pb.BolgService_ConnectServer,
	in *pb.RoomMessage_StartGameReq,
) error {
	rid, pid, err := game.ParseFromToken(in.StartGameReq.Token)
	if errors.Is(err, game.ErrInvalidToken) {
		return status.Error(codes.Unauthenticated, "token error")
	}

	r, err := svc.roomDB.Get(rid)
	if err != nil {
		if errors.Is(err, game.ErrRoomDBNotFound) {
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

	if err := svc.roomDB.Update(r); err != nil {
		return svc.internalError(fmt.Sprintf("Error handleStartGameReq roomdb.Update: %s", err.Error()))
	}

	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_StartGameMsg{
			StartGameMsg: &pb.StartGameMessage{
				Room: r.ToProtoRoom(),
			},
		},
	}
	if errs := r.Broadcast(game.FilterNothing(), out); len(errs) != 0 {
		svc.logger.Error(fmt.Sprintf("broadcast error: %+v", errs))
	}

	return nil
}

func (svc *Service) handleNotifyReceivingReq(
	stream pb.BolgService_ConnectServer,
	in *pb.RoomMessage_NotifyReceivingReq,
) error {
	rid, pid, err := game.ParseFromToken(in.NotifyReceivingReq.Token)
	if errors.Is(err, game.ErrInvalidToken) {
		return status.Error(codes.Unauthenticated, "token error")
	}

	r, err := svc.roomDB.Get(rid)
	if err != nil {
		if errors.Is(err, game.ErrRoomDBNotFound) {
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
	receiver.Hp = game.Damage(sender.Power, receiver.Hp)
	if game.IsDead(receiver.Hp) {
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
	if errs := r.Broadcast(game.FilterNothing(), out); len(errs) != 0 {
		svc.logger.Error(fmt.Sprintf("broadcast error(NotifyReceivingMsg): %+v", errs))
	}

	winner, done := game.SurvivalJudge(r.Players)
	if !done {
		return nil
	}

	// ゲームリザルト通知
	out = &pb.RoomMessage{
		Data: &pb.RoomMessage_SurvivalResultMsg{
			SurvivalResultMsg: &pb.SurvivalResultMessage{
				Winner:    winner.ToProtoPlayer(),
				Personals: game.CreateSurvivalPersonalResults(r.Players),
			},
		},
	}
	if errs := r.Broadcast(game.FilterNothing(), out); len(errs) != 0 {
		svc.logger.Error(fmt.Sprintf("broadcast error(NotifyReceivingMsg): %+v", errs))
	}

	r.GameStart = false
	r.InitPlayers()
	if err := svc.roomDB.Update(r); err != nil {
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

func (svc *Service) GetStamina(ctx context.Context, in *pb.GetStaminaRequest) (*pb.Stamina, error) {
	a, ok := account.GetAccountFromCtx(ctx)
	if !ok {
		return nil, svc.internalError("svc.GetStamina: failed to get account from context")
	}

	s, err := svc.staminaDB.Get(a.ID)
	if errors.Is(err, kakin.ErrStaminaDBNotFound) {
		return nil, svc.internalError(fmt.Sprintf("svc.GetStamina: %s", err.Error()))
	}

	s.Recovery()
	s.UpdateRecoveryTime()
	if err = svc.staminaDB.Update(s); errors.Is(err, kakin.ErrStaminaDBNotFound) {
		return nil, svc.internalError(fmt.Sprintf("svc.GetStamina update error: %s", err.Error()))
	}

	return s.ToProtoStamina(), nil
}

func (svc *Service) UseStamina(ctx context.Context, in *pb.UseStaminaRequest) (*pb.UseStaminaResponse, error) {
	a, ok := account.GetAccountFromCtx(ctx)
	if !ok {
		return nil, svc.internalError("svc.GetStamina: failed to get account from context")
	}

	s, err := svc.staminaDB.Get(a.ID)
	if errors.Is(err, kakin.ErrStaminaDBNotFound) {
		return nil, svc.internalError(fmt.Sprintf("svc.GetStamina: %s", err.Error()))
	}

	if ok := s.Use(); !ok {
		return nil, status.Error(codes.FailedPrecondition, "failed to use stamina because count is zero")
	}
	s.UpdateRecoveryTime()
	if err = svc.staminaDB.Update(s); errors.Is(err, kakin.ErrStaminaDBNotFound) {
		return nil, svc.internalError(fmt.Sprintf("svc.GetStamina update error: %s", err.Error()))
	}

	return &pb.UseStaminaResponse{Stamina: s.ToProtoStamina()}, nil
}

func (svc *Service) RecoverStamina(ctx context.Context, in *pb.RecoverStaminaRequest) (*pb.RecoverStaminaResponse, error) {
	a, ok := account.GetAccountFromCtx(ctx)
	if !ok {
		return nil, svc.internalError("svc.GetStamina: failed to get account from context")
	}

	s, err := svc.staminaDB.Get(a.ID)
	if errors.Is(err, kakin.ErrStaminaDBNotFound) {
		return nil, svc.internalError(fmt.Sprintf("svc.GetStamina: %s", err.Error()))
	}

	s.ForceRecovery(int(in.Count))
	s.UpdateRecoveryTime()
	if err = svc.staminaDB.Update(s); errors.Is(err, kakin.ErrStaminaDBNotFound) {
		return nil, svc.internalError(fmt.Sprintf("svc.GetStamina update error: %s", err.Error()))
	}

	return &pb.RecoverStaminaResponse{Stamina: s.ToProtoStamina()}, nil
}

//------------------------------------------------------------------------------
// Helpers
//------------------------------------------------------------------------------
func (svc *Service) internalError(msg string) error {
	svc.logger.Error(msg)
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

//------------------------------------------------------------------------------
// Main
//------------------------------------------------------------------------------
func main() {
	port, err := net.Listen("tcp", ":50051")
	if err != nil {
		panic(err)
	}
	defer port.Close()

	conf := zap.NewProductionConfig()
	conf.OutputPaths = append(conf.OutputPaths, "./bolg.log")
	conf.ErrorOutputPaths = append(conf.ErrorOutputPaths, "./bolg.log")
	logger, err := conf.Build()
	if err != nil {
		panic(err)
	}
	defer logger.Sync() // flushes buffer, if any

	accountDB := account.NewMemoryAccountDB()
	if err := account.InjectAccounts(accountDB); err != nil {
		panic(err)
	}

	staminaDB := kakin.NewMemoryStaminaDB()
	if err := kakin.InjectStamins(staminaDB); err != nil {
		panic(err)
	}

	svc := &Service{
		roomDB:    game.NewMemoryRoomDB(),
		accountDB: accountDB,
		staminaDB: staminaDB,
		logger:    logger,
	}

	srv := grpc.NewServer(grpc.UnaryInterceptor(account.Auth(svc.accountDB)))
	pb.RegisterBolgServiceServer(srv, svc)
	if err := srv.Serve(port); err != nil {
		panic(err)
	}
}
