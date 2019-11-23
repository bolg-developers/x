package room

import (
	"errors"
	"sync"

	"github.com/bolg-developers/x/projects/bolg/server/pb"
)

type Streams struct {
	sync.Mutex
	streams []pb.BolgService_ConnectServer
}

func NewStreams(size int) *Streams {
	return &Streams{
		streams: make([]pb.BolgService_ConnectServer, 0, size),
	}
}

func (s *Streams) Append(stream pb.BolgService_ConnectServer) {
	s.streams = append(s.streams, stream)
}

func (s *Streams) Delete(stream pb.BolgService_ConnectServer) error {
	for i, strm := range s.streams {
		if strm == stream {
			s.streams = append(s.streams[:i], s.streams[i:]...)
			return nil
		}
	}
	return errors.New("streams: not foudn stream")
}

func (s *Streams) Broadcast(msg *pb.RoomMessage) ([]pb.BolgService_ConnectServer, error) {
	ret := make([]pb.BolgService_ConnectServer, 0, len(s.streams))
	var retErr error
	for _, strm := range s.streams {
		if err := strm.Send(msg); err != nil {
			retErr = err
			ret = append(ret, strm)
		}
	}
	return ret, retErr
}

func (s *Streams) BroadcastOthers(stream pb.BolgService_ConnectServer, msg *pb.RoomMessage) ([]pb.BolgService_ConnectServer, error) {
	ret := make([]pb.BolgService_ConnectServer, 0, len(s.streams))
	var retErr error
	for _, strm := range s.streams {
		if strm == stream {
			continue
		}
		if err := strm.Send(msg); err != nil {
			retErr = err
			ret = append(ret, strm)
		}
	}
	return ret, retErr
}
