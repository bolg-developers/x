package game

import (
	"errors"
	"sync"
)

var (
	ErrRoomDBAlreadyExists = errors.New("roomdb: already exists room")
	ErrRoomDBNotFound      = errors.New("roomdb: not found room")
)

type RoomDB interface {
	Get(id int64) (*Room, error)
	Create(*Room) error
	Update(*Room) error
}

// TODO: test
type roomIDAllocator struct {
	ids []int64
	cnt int64
	mu  sync.Mutex
}

func newRoomIDAllocator() *roomIDAllocator {
	return &roomIDAllocator{
		ids: make([]int64, 0, 9999),
	}
}

func (a *roomIDAllocator) Allocate() (int64, error) {
	a.mu.Lock()
	defer a.mu.Unlock()

	first := a.cnt
	for a.exists() {
		a.cnt++
		if a.cnt == 9999 {
			a.cnt = 0
		}
		if a.cnt == first {
			return 0, errors.New("roomdb: room is full")
		}
	}
	a.ids = append(a.ids, a.cnt)
	return a.cnt, nil
}

func (a *roomIDAllocator) exists() bool {
	for _, id := range a.ids {
		if a.cnt == id {
			return true
		}
	}
	return false
}
