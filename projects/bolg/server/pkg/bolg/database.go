package bolg

import (
	"errors"
)

var (
	ErrNotFound      = errors.New("database: not found")
	ErrAlreadyExists = errors.New("database: already exists")
)

type RoomDatabase interface {
	Create(*Room) error
	Get(id int64) (*Room, error)
	CreatePlayer(roomID int64, p *Player) error
}
