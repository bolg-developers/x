package bolg

import (
	"fmt"
	"sync"
)

type idPoolManager struct {
	sync.Mutex
	idPoolMap map[int64]idPool
}

func newIDPoolManager() *idPoolManager {
	return &idPoolManager{
		idPoolMap: make(map[int64]idPool, maxRoom),
	}
}

func (idm *idPoolManager) create(roomID int64) error {
	idm.Lock()
	defer idm.Unlock()
	if _, ok := idm.idPoolMap[roomID]; ok {
		return fmt.Errorf("idMap[%d] is already exists", roomID)
	}
	idm.idPoolMap[roomID] = newIdPool(maxPlayer)
	return nil
}

func (idm *idPoolManager) getID(roomID int64) (int64, error) {
	idm.Lock()
	defer idm.Unlock()
	pool, ok := idm.idPoolMap[roomID]
	if !ok {
		return 0, fmt.Errorf("idPoolMap[%d] is not created", roomID)
	}
	id, ok := pool.get()
	if !ok {
		return 0, fmt.Errorf("all id are used(roomid=%d)", roomID)
	}
	return id, nil
}

type idPool map[int64]bool

func newIdPool(size int) idPool {
	pool := make(idPool, size)
	for i := 0; i < size; i++ {
		pool[int64(i)] = true
	}
	return pool
}

func (idp idPool) get() (int64, bool) {
	for n, unuse := range idp {
		if unuse {
			idp[n] = false
			return n, true
		}
	}
	return 0, false
}
