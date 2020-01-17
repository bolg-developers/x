package bolg

import (
	"context"
	"fmt"
	"io"
	"log"

	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"

	"github.com/bolg-developers/x/projects/bolg/server/pb"
)

/* NOTE: validationはここでやる。 */

type Service struct {
	roomDB RoomDatabase
	nm     *numberManager
	sm     *streamsManager
	idpm   *idPoolManager
	jm     *judgementManager
}

func NewService() pb.BolgServiceServer {
	return &Service{
		roomDB: newMemoryRoomDatabase(),
		nm:     newNumberManager(),
		sm:     newStreamsManager(),
		idpm:   newIDPoolManager(),
		jm:     newJudgementManager(),
	}
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
			log.Println("Recieve CreateAndJoinRoomReq")
			if err := svc.handleCreateAndJoinRoomReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_JoinRoomReq:
			log.Println("Recieve JoinRoomReq")
			if err := svc.handleJoinRoomReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_NotifyReceivingReq:
			log.Println("Recieve NotifyReceivingReq")
			if err := svc.handleNotifyReceivingReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_StartGameReq:
			log.Println("Recieve StartGameReq")
			if err := svc.handleStartGameReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_UpdateWeaponReq:
			log.Println("Recieve UpdateWeaponReq")
			if err := svc.handleUpdateWeaponReq(stream, msg); err != nil {
				return err
			}
		case *pb.RoomMessage_ReadyReq:
			log.Println("Recieve ReadyReq")
			if err := svc.handleReadyReq(stream, msg); err != nil {
				return err
			}
		default:
			return status.Error(codes.InvalidArgument, "invalid room message")
		}
	}
}

// TODO: cleanup instances when error occurred.
func (svc *Service) handleCreateAndJoinRoomReq(stream pb.BolgService_ConnectServer, in *pb.RoomMessage_CreateAndJoinRoomReq) error {
	// TODO: 認証機構を実装してtokenからUser情報を取得するようにする
	pname := in.CreateAndJoinRoomReq.PlayerName
	if pname == "" {
		return sendRoomMessageError(stream, codes.InvalidArgument, "player name is empty")
	}
	rid, ok := svc.nm.createNum()
	if !ok {
		return status.Error(codes.ResourceExhausted, "no room")
	}
	if err := svc.jm.create(rid, newSurivalJudgement()); err != nil {
		return toGRPCError(err)
	}
	if err := svc.idpm.create(rid); err != nil {
		return toGRPCError(err)
	}
	pid, err := svc.idpm.getID(rid)
	if err != nil {
		return toGRPCError(err)
	}
	p := NewPlayer(pid, pname)
	r := NewRoom(rid)
	r.OwnerId = pid
	if err := svc.roomDB.Create(r); err != nil {
		return toGRPCError(err)
	}
	if err := svc.roomDB.CreatePlayer(rid, p); err != nil {
		return toGRPCError(err)
	}
	r, err = svc.roomDB.Get(rid)
	if err != nil {
		return toGRPCError(err)
	}
	if err := svc.sm.createStreams(rid); err != nil {
		return toGRPCError(err)
	}
	if err := svc.sm.appendStream(rid, stream); err != nil {
		return toGRPCError(err)
	}
	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_CreateAndJoinRoomResp{
			CreateAndJoinRoomResp: &pb.CreateAndJoinRoomResponse{
				Room:  &r.Room,
				Token: createToken(rid, pid),
			},
		},
	}
	if err := stream.Send(out); err != nil {
		return err
	}
	return nil
}

func (svc *Service) handleJoinRoomReq(stream pb.BolgService_ConnectServer, in *pb.RoomMessage_JoinRoomReq) error {
	// TODO: 認証機構を実装してtokenからUser情報を取得するようにする
	pname := in.JoinRoomReq.PlayerName
	if pname == "" {
		return sendRoomMessageError(stream, codes.InvalidArgument, "player name is empty")
	}
	rid := in.JoinRoomReq.RoomId
	r, err := svc.roomDB.Get(rid)
	if err != nil {
		return toGRPCError(err)
	}
	if r.GameStart {
		return sendRoomMessageError(stream, codes.FailedPrecondition, "game is already starting")
	}
	pid, err := svc.idpm.getID(rid)
	if err != nil {
		return toGRPCError(err)
	}
	p := NewPlayer(pid, pname)
	if err := svc.roomDB.CreatePlayer(rid, p); err != nil {
		return toGRPCError(err)
	}
	r, err = svc.roomDB.Get(rid)
	if err != nil {
		return toGRPCError(err)
	}
	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_JoinRoomResp{
			JoinRoomResp: &pb.JoinRoomResponse{
				Room:  &r.Room,
				Token: createToken(rid, pid),
			},
		},
	}
	if err := stream.Send(out); err != nil {
		return err
	}
	if err := svc.sm.appendStream(rid, stream); err != nil {
		return toGRPCError(err)
	}
	out = &pb.RoomMessage{
		Data: &pb.RoomMessage_JoinRoomMsg{
			JoinRoomMsg: &pb.JoinRoomMessage{
				Player: &p.Player,
			},
		},
	}
	if _, err := svc.sm.Broadcasts(rid, stream, out); err != nil {
		return toGRPCError(err)
	}
	return nil
}

func (svc *Service) handleNotifyReceivingReq(stream pb.BolgService_ConnectServer, in *pb.RoomMessage_NotifyReceivingReq) error {
	rid, pid, err := parseFromToken(in.NotifyReceivingReq.Token)
	if err != nil {
		return toGRPCError(err)
	}
	room, err := svc.roomDB.Get(rid)
	if err != nil {
		return toGRPCError(err)
	}
	if !room.GameStart {
		return sendRoomMessageError(stream, codes.FailedPrecondition, "game is not starting")
	}
	receiver, err := svc.roomDB.GetPlayer(rid, pid)
	if err != nil {
		return toGRPCError(err)
	}
	sender, err := svc.roomDB.GetPlayer(rid, in.NotifyReceivingReq.PlayerId)
	if err != nil {
		return toGRPCError(err)
	}
	kill, err := damage(receiver, sender)
	if err != nil {
		return sendRoomMessageError(stream, codes.FailedPrecondition, err.Error())
	}
	if err := svc.roomDB.UpdatePlayer(rid, receiver); err != nil {
		return toGRPCError(err)
	}
	if err := svc.roomDB.UpdatePlayer(rid, sender); err != nil {
		return toGRPCError(err)
	}
	var killerName string
	if kill {
		killerName = sender.Name
	}
	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_NotifyReceivingMsg{
			NotifyReceivingMsg: &pb.NotifyReceivingMessage{
				Player:     &receiver.Player,
				KillerName: killerName,
			},
		},
	}
	if _, err := svc.sm.Broadcasts(rid, stream, out); err != nil {
		return toGRPCError(err)
	}
	players, err := svc.roomDB.ListPlayers(rid)
	if err != nil {
		return toGRPCError(err)
	}
	winners, done, err := svc.jm.judge(rid, players)
	if err != nil {
		return toGRPCError(err)
	}
	if !done {
		return nil
	}
	out = &pb.RoomMessage{
		Data: &pb.RoomMessage_SurvivalResultMsg{
			SurvivalResultMsg: &pb.SurvivalResultMessage{
				Winner:    &winners[0].Player,
				Personals: players.ToSurivalPersonalResults(),
			},
		},
	}
	if _, err := svc.sm.Broadcasts(rid, nil, out); err != nil {
		return toGRPCError(err)
	}
	room, err = svc.roomDB.Get(rid)
	if err != nil {
		return toGRPCError(err)
	}
	room.GameStart = false
	if err := svc.roomDB.Update(room); err != nil {
		return toGRPCError(err)
	}
	players, err = svc.roomDB.ListPlayers(rid)
	if err != nil {
		return toGRPCError(err)
	}
	changeReadyFalse(players)
	for _, p := range players {
		if err := svc.roomDB.UpdatePlayer(rid, p); err != nil {
			return toGRPCError(err)
		}
	}
	return nil
}

func (svc *Service) handleStartGameReq(stream pb.BolgService_ConnectServer, in *pb.RoomMessage_StartGameReq) error {
	rid, pid, err := parseFromToken(in.StartGameReq.Token)
	if err != nil {
		return toGRPCError(err)
	}
	room, err := svc.roomDB.Get(rid)
	if err != nil {
		return toGRPCError(err)
	}
	if pid != room.OwnerId {
		return sendRoomMessageError(stream, codes.FailedPrecondition, "your are not room owner")
	}
	players, err := svc.roomDB.ListPlayers(rid)
	if err != nil {
		return toGRPCError(err)
	}
	if len(players.NotReadyPlayers()) > 0 {
		return sendRoomMessageError(stream, codes.FailedPrecondition, "some players are not ready yet")
	}
	if room.GameStart {
		return sendRoomMessageError(stream, codes.FailedPrecondition, "game is already starting")
	}
	room.GameStart = true
	if err := svc.roomDB.Update(room); err != nil {
		return toGRPCError(err)
	}
	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_StartGameMsg{
			StartGameMsg: &pb.StartGameMessage{
				Room: &room.Room,
			},
		},
	}
	if _, err := svc.sm.Broadcasts(rid, nil, out); err != nil {
		return toGRPCError(err)
	}
	return nil
}

func (svc *Service) handleUpdateWeaponReq(stream pb.BolgService_ConnectServer, in *pb.RoomMessage_UpdateWeaponReq) error {
	rid, pid, err := parseFromToken(in.UpdateWeaponReq.Token)
	if err != nil {
		return toGRPCError(err)
	}
	room, err := svc.roomDB.Get(rid)
	if err != nil {
		return toGRPCError(err)
	}
	if room.GameStart {
		return sendRoomMessageError(stream, codes.FailedPrecondition, "game is already starting")
	}
	player, err := svc.roomDB.GetPlayer(rid, pid)
	if err != nil {
		return toGRPCError(err)
	}
	player.Attack = in.UpdateWeaponReq.Attack
	if err := svc.roomDB.UpdatePlayer(rid, player); err != nil {
		return toGRPCError(err)
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

func (svc *Service) handleReadyReq(stream pb.BolgService_ConnectServer, in *pb.RoomMessage_ReadyReq) error {
	rid, pid, err := parseFromToken(in.ReadyReq.Token)
	if err != nil {
		return toGRPCError(err)
	}
	room, err := svc.roomDB.Get(rid)
	if err != nil {
		return toGRPCError(err)
	}
	if room.GameStart {
		return sendRoomMessageError(stream, codes.FailedPrecondition, "game is already starting")
	}
	player, err := svc.roomDB.GetPlayer(rid, pid)
	if err != nil {
		return toGRPCError(err)
	}
	if player.Ready {
		return sendRoomMessageError(stream, codes.FailedPrecondition, "you are already ready")
	}
	player.Ready = true
	if err := svc.roomDB.UpdatePlayer(rid, player); err != nil {
		return toGRPCError(err)
	}
	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_ReadyMsg{
			ReadyMsg: &pb.ReadyMessage{
				PlayerId: player.Id,
			},
		},
	}
	if _, err := svc.sm.Broadcasts(rid, nil, out); err != nil {
		return toGRPCError(err)
	}
	return nil
}

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

func toGRPCError(err error) error {
	if err == nil {
		return err
	}
	switch err {
	case ErrNotFound:
		return status.Error(codes.NotFound, err.Error())
	case ErrAlreadyExists:
		return status.Error(codes.AlreadyExists, err.Error())
	case ErrInvalidToken:
		return status.Error(codes.Unauthenticated, err.Error())
	case ErrHPisZero:
		return status.Error(codes.FailedPrecondition, err.Error())
	default:
		return status.Error(codes.Internal, err.Error())
	}
}

func sendRoomMessageError(stream pb.BolgService_ConnectServer, code codes.Code, msg string) error {
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
