package memory

import (
	"sync"

	"github.com/bolg-developers/x/projects/bolg/server/pkg/room"
	"github.com/bolg-developers/x/projects/bolg/server/pkg/room/database"
)

type PlayerDatabase struct {
	sync.Mutex
	playersMap map[int64][]room.Player
}

func NewPlayerDatabase(playerCnt int) database.PlayerDatabase {
	return &PlayerDatabase{playersMap: make(map[int64][]room.Player, playerCnt)}
}

func (db *PlayerDatabase) Get(roomID, playerID int64) (*room.Player, error) {
	db.Lock()
	defer db.Unlock()
	return db.get(roomID, playerID)
}

func (db *PlayerDatabase) get(roomID, playerID int64) (*room.Player, error) {
	players, ok := db.playersMap[roomID]
	if !ok {
		return nil, database.ErrFailedPrecondition
	}
	for _, p := range players {
		if p.Id == playerID {
			ret := p
			return &ret, nil
		}
	}
	return nil, database.ErrNotFound
}

func (db *PlayerDatabase) Create(roomID int64, p *room.Player) error {
	db.Lock()
	defer db.Unlock()
	return db.create(roomID, p)
}

func (db *PlayerDatabase) create(roomID int64, p *room.Player) error {
	_, err := db.get(roomID, p.Id)
	if err == nil {
		return database.ErrAlreadyExists
	}
	if err != database.ErrNotFound {
		if err == database.ErrFailedPrecondition {
			const cap = 16 // not yet optimized
			db.playersMap[roomID] = make([]room.Player, 0, cap)
		} else {
			return err
		}
	}
	db.playersMap[roomID] = append(db.playersMap[roomID], *p)
	return nil
}

func (db *PlayerDatabase) List(roomID int64) (room.Players, error) {
	db.Lock()
	defer db.Unlock()
	return db.list(roomID)
}

func (db *PlayerDatabase) list(roomID int64) (room.Players, error) {
	players, ok := db.playersMap[roomID]
	if !ok {
		return nil, database.ErrFailedPrecondition
	}
	ret := make(room.Players, len(players))
	for i, p := range players {
		pp := p
		ret[i] = &pp
	}
	return ret, nil
}

func (db *PlayerDatabase) Update(roomID int64, p *room.Player) error {
	db.Lock()
	defer db.Unlock()
	return db.update(roomID, p)
}

func (db *PlayerDatabase) update(roomID int64, p *room.Player) error {
	var found bool
	players := db.playersMap[roomID]
	for i, player := range players {
		if player.Id == p.Id {
			players[i] = *p
			found = true
			break
		}
	}
	if !found {
		return database.ErrNotFound
	}
	db.playersMap[roomID] = players
	return nil
}
