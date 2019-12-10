package memory

import (
	"sync"

	"github.com/bolg-developers/x/projects/bolg/server/pkg/room"
	"github.com/bolg-developers/x/projects/bolg/server/pkg/room/database"
)

type RoomDatabase struct {
	sync.Mutex
	roomMap map[int64]room.Room
}

func NewRoomDatabase(roomCnt int) database.RoomDatabase {
	return &RoomDatabase{roomMap: make(map[int64]room.Room, roomCnt)}
}

func (db *RoomDatabase) Create(r *room.Room) error {
	db.Lock()
	defer db.Unlock()
	return db.create(r)
}

func (db *RoomDatabase) create(r *room.Room) error {
	_, err := db.get(r.Id)
	if err == nil {
		return database.ErrAlreadyExists
	}
	if err != database.ErrNotFound {
		return err
	}
	db.roomMap[r.Id] = *r
	return nil
}

func (db *RoomDatabase) Get(id int64) (*room.Room, error) {
	db.Lock()
	defer db.Unlock()
	return db.get(id)
}

func (db *RoomDatabase) get(id int64) (*room.Room, error) {
	r, ok := db.roomMap[id]
	if !ok {
		return nil, database.ErrNotFound
	}
	ret := r
	return &ret, nil
}

func (db *RoomDatabase) Update(r *room.Room) error {
	db.Lock()
	defer db.Unlock()
	return db.update(r)
}

func (db *RoomDatabase) update(r *room.Room) error {
	_, err := db.get(r.Id)
	if err != nil {
		return err
	}
	db.roomMap[r.Id] = *r
	return nil
}
