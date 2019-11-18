package bolg

import (
	"fmt"
	"sync"

	"github.com/bolg-developers/x/projects/bolg/server/pb"
)

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

func (sm *streamsManager) appendStream(roomID int64, stream pb.BolgService_CreateAndJoinRoomServer) error {
	sm.Lock()
	defer sm.Unlock()
	streams, ok := sm.streamsMap[roomID]
	if !ok {
		return fmt.Errorf("streamsMap[%d] is not found", roomID)
	}
	streams = append(streams, stream)
	return nil
}

type streams []pb.BolgService_CreateAndJoinRoomServer
