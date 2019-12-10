package database

import (
	"errors"

	"github.com/bolg-developers/x/projects/bolg/server/pkg/room"
)

var (
	ErrNotFound           = errors.New("database: not found")
	ErrAlreadyExists      = errors.New("database: already exists")
	ErrFailedPrecondition = errors.New("database: failed precondition")
)

type RoomDatabase interface {
	Create(*room.Room) error
	Get(id int64) (*room.Room, error)
	Update(*room.Room) error
}

type PlayerDatabase interface {
	Create(roomID int64, p *room.Player) error
	List(roomID int64) (room.Players, error)
	Get(roomID, playerID int64) (*room.Player, error)
	Update(roomID int64, p *room.Player) error
}
