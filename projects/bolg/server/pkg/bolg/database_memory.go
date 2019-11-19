package bolg

import (
	"errors"
	"fmt"
	"sync"
)

type memoryRoomDatabase struct {
	sync.Mutex
	roomMap    map[int64]*Room
	playersMap map[int64]Players
}

func newMemoryRoomDatabase() RoomDatabase {
	return &memoryRoomDatabase{
		roomMap:    make(map[int64]*Room, maxRoom),
		playersMap: make(map[int64]Players, maxRoom),
	}
}

func (db *memoryRoomDatabase) Create(r *Room) error {
	db.Lock()
	defer db.Unlock()
	_, ok := db.roomMap[r.Id]
	if ok {
		return ErrAlreadyExists
	}
	db.roomMap[r.Id] = r
	db.playersMap[r.Id] = make(Players, 0, maxPlayer)
	return nil
}

func (db *memoryRoomDatabase) Get(id int64) (*Room, error) {
	db.Lock()
	defer db.Unlock()
	return db.get(id)
}

func (db *memoryRoomDatabase) get(id int64) (*Room, error) {
	r, ok := db.roomMap[id]
	if !ok {
		return nil, ErrNotFound
	}
	pm, ok := db.playersMap[id]
	if !ok {
		return nil, errors.New(fmt.Sprintf("playersMap[%d] is not created", id))
	}
	r.Players = pm.ToPBPlayers()
	return r, nil
}

func (db *memoryRoomDatabase) CreatePlayer(roomID int64, p *Player) error {
	db.Lock()
	defer db.Unlock()
	_, err := db.get(roomID)
	if err != nil {
		return err
	}
	_, ok := db.playersMap[roomID]
	if !ok {
		return fmt.Errorf("playersMap[%d] is not created", roomID)
	}
	if _, err := db.getPlayer(roomID, p.Id); err == nil {
		return ErrAlreadyExists
	} else {
		if !errors.Is(err, ErrNotFound) {
			return fmt.Errorf("failed to get player: %s", err.Error())
		}
	}
	db.playersMap[roomID] = append(db.playersMap[roomID], p)
	return nil
}

func (db *memoryRoomDatabase) GetPlayer(roomID, playerID int64) (*Player, error) {
	db.Lock()
	defer db.Unlock()
	return db.getPlayer(roomID, playerID)
}

func (db *memoryRoomDatabase) getPlayer(roomID, playerID int64) (*Player, error) {
	players, ok := db.playersMap[roomID]
	if !ok {
		return nil, fmt.Errorf("playersMap[%d] is not created", roomID)
	}
	for _, p := range players {
		if p.Id == playerID {
			return p, nil
		}
	}
	return nil, ErrNotFound
}

func (db *memoryRoomDatabase) UpdatePlayer(roomID int64, player *Player) error {
	db.Lock()
	defer db.Unlock()
	players, ok := db.playersMap[roomID]
	if !ok {
		return fmt.Errorf("playersMap[%d] is not created", roomID)
	}
	for i, p := range players {
		if p.Id == player.Id {
			players[i] = player
			return nil
		}
	}
	return fmt.Errorf("not found: (roomID, playerID)=(%d, %d)", roomID, player.Id)
}
