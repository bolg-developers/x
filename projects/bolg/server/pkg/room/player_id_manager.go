package room

import (
	"errors"
	"sync"
)

type PlayerIDManager struct {
	sync.Mutex
	idMap map[int64]bool
}

func NewPlayerIDManager(size int) *PlayerIDManager {
	idMap := make(map[int64]bool, size)
	for i := 0; i < size; i++ {
		idMap[int64(i)] = false
	}
	return &PlayerIDManager{
		idMap: idMap,
	}
}

func (pidm *PlayerIDManager) Get() (int64, error) {
	pidm.Lock()
	defer pidm.Unlock()
	for id, use := range pidm.idMap {
		if !use {
			pidm.idMap[id] = true
			return id, nil
		}
	}
	return 0, errors.New("player id manager: not found")
}

func (pidm *PlayerIDManager) Delete(id int64) error {
	pidm.Lock()
	defer pidm.Unlock()
	_, exists := pidm.idMap[id]
	if !exists {
		return errors.New("player id manager: not found id")
	}
	delete(pidm.idMap, id)
	return nil
}
