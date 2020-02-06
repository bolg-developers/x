package game

import (
	"fmt"
	"sync"
)

type memoryRoomDB struct {
	rooms map[int64]Room
	mu    sync.Mutex
	a     *roomIDAllocator
}

func NewMemoryRoomDB() RoomDB {
	return &memoryRoomDB{
		rooms: make(map[int64]Room, 100),
		a:     newRoomIDAllocator(),
	}
}

func (db *memoryRoomDB) Get(id int64) (*Room, error) {
	db.mu.Lock()
	defer db.mu.Unlock()

	r, ok := db.rooms[id]
	if !ok {
		return nil, fmt.Errorf("%w with id %d", ErrRoomDBNotFound, id)
	}

	return &r, nil
}

func (db *memoryRoomDB) Create(r *Room) error {
	db.mu.Lock()
	defer db.mu.Unlock()

	id, err := db.a.Allocate()
	if err != nil {
		return err
	}
	r.ID = id

	_, ok := db.rooms[r.ID]
	if ok {
		return fmt.Errorf("%w with id %d", ErrRoomDBAlreadyExists, r.ID)
	}

	db.rooms[r.ID] = *r

	return nil
}

func (db *memoryRoomDB) Update(r *Room) error {
	db.mu.Lock()
	defer db.mu.Unlock()

	_, ok := db.rooms[r.ID]
	if !ok {
		return fmt.Errorf("%w with id %d", ErrRoomDBNotFound, r.ID)
	}
	db.rooms[r.ID] = *r

	return nil
}
