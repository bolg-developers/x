package bolg

import (
	"errors"
	"fmt"
	"sync"

	"github.com/bolg-developers/x/projects/bolg/server/pb"
)

// TODO: playerの属性によってフィルターできるようにする
type streamsManager struct {
	sync.Mutex
	streamsMap map[int64]streams
}

func newStreamsManager() *streamsManager {
	return &streamsManager{
		streamsMap: make(map[int64]streams, maxRoom),
	}
}

func (sm *streamsManager) createStreams(roomID int64) error {
	sm.Lock()
	defer sm.Unlock()
	if _, ok := sm.streamsMap[roomID]; ok {
		return fmt.Errorf("streamsMap[%d] is already exists", roomID)
	}
	sm.streamsMap[roomID] = make(streams, 0, maxPlayer)
	return nil
}

func (sm *streamsManager) appendStream(roomID int64, stream pb.BolgService_ConnectServer) error {
	sm.Lock()
	defer sm.Unlock()
	_, ok := sm.streamsMap[roomID]
	if !ok {
		return fmt.Errorf("streamsMap[%d] is not found", roomID)
	}
	sm.streamsMap[roomID] = append(sm.streamsMap[roomID], stream)
	return nil
}

func (sm *streamsManager) Broadcasts(roomId int64, ignore pb.BolgService_ConnectServer, msg *pb.RoomMessage) (streams, error) {
	sm.Lock()
	defer sm.Unlock()
	streams := make(streams, 0, len(sm.streamsMap[roomId]))
	for _, s := range sm.streamsMap[roomId] {
		if s == ignore {
			continue
		}
		if err := s.Send(msg); err != nil {
			streams = append(streams, s)
		}
	}
	var err error
	if len(streams) != 0 {
		err = errors.New("failed to broadcast")
	}
	return streams, err
}

type streams []pb.BolgService_ConnectServer
