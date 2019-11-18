package bolg

import (
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
}

func NewService() pb.BolgServiceServer {
	return &Service{
		roomDB: newMemoryRoomDatabase(),
		nm:     newNumberManager(),
		sm:     newStreamsManager(),
		idpm:   newIDPoolManager(),
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
		pname = "名無しの桝井隆治(仮)"
	}
	log.Println("Recieve CreateAndJoinRoomReq")
	rid, ok := svc.nm.createNum()
	if !ok {
		return status.Error(codes.ResourceExhausted, "room is full")
	}
	log.Println("Created room number:", rid)
	if err := svc.idpm.create(rid); err != nil {
		return toGRPCError(err)
	}
	log.Println("Created id pool")
	pid, err := svc.idpm.getID(rid)
	if err != nil {
		return toGRPCError(err)
	}
	log.Println("Got plyaer id:", pid)
	p := NewPlayer(pid, pname)
	r := NewRoom(rid)
	r.OwnerId = pid
	if err := svc.roomDB.Create(r); err != nil {
		return toGRPCError(err)
	}
	log.Println("Created room")
	if err := svc.roomDB.CreatePlayer(rid, p); err != nil {
		return toGRPCError(err)
	}
	log.Println("Created player")
	r, err = svc.roomDB.Get(rid)
	if err != nil {
		return toGRPCError(err)
	}
	if err := svc.sm.createStreams(rid); err != nil {
		return toGRPCError(err)
	}
	log.Println("Created streams")
	if err := svc.sm.appendStream(rid, stream); err != nil {
		return toGRPCError(err)
	}
	log.Println("Appended stream")
	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_CreateAndJoinRoomResp{
			CreateAndJoinRoomResp: &pb.CreateAndJoinRoomResponse{
				Room: &r.Room,
			},
		},
	}
	if err := stream.Send(out); err != nil {
		return err
	}
	log.Println("Sent message")
	return nil
}

func (svc *Service) handleJoinRoomReq(stream pb.BolgService_ConnectServer, in *pb.RoomMessage_JoinRoomReq) error {
	// TODO: 認証機構を実装してtokenからUser情報を取得するようにする
	pname := in.JoinRoomReq.PlayerName
	if pname == "" {
		pname = "名無しの桝井隆治(仮)"
	}
	rid := in.JoinRoomReq.RoomId
	pid, err := svc.idpm.getID(rid)
	if err != nil {
		return toGRPCError(err)
	}
	log.Println("Got plyaer id:", pid)
	p := NewPlayer(pid, pname)
	if err := svc.roomDB.CreatePlayer(rid, p); err != nil {
		return toGRPCError(err)
	}
	r, err := svc.roomDB.Get(rid)
	if err != nil {
		return toGRPCError(err)
	}
	out := &pb.RoomMessage{
		Data: &pb.RoomMessage_JoinRoomResp{
			JoinRoomResp: &pb.JoinRoomResponse{
				Room: &r.Room,
			},
		},
	}
	if err := stream.Send(out); err != nil {
		return err
	}
	log.Println("Sent message")
	if err := svc.sm.appendStream(rid, stream); err != nil {
		return toGRPCError(err)
	}
	log.Println("Appended stream")
	out = &pb.RoomMessage{
		Data: &pb.RoomMessage_JoinRoomMsg{
			JoinRoomMsg: &pb.JoinRoomMessage{
				Player: &p.Player,
			},
		},
	}
	if streams := svc.sm.Broadcasts(rid, stream, out); len(streams) != 0 {
		log.Println("Failed to broadcast")
	}
	log.Println("Sent message others")
	return nil
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
	default:
		return status.Error(codes.Internal, err.Error())
	}
}
