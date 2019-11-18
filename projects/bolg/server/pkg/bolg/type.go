package bolg

import (
	"github.com/bolg-developers/x/projects/bolg/server/pb"
)

const (
	maxPlayer = 16
	maxRoom   = 30
	initHP    = 100
)

type Room struct {
	pb.Room
}

func NewRoom(id int64) *Room {
	return &Room{
		Room: pb.Room{
			Id: id,
		},
	}
}

type Player struct {
	pb.Player
	Attack int64
}

func NewPlayer(id int64, name string) *Player {
	return &Player{
		Player: pb.Player{
			Id:   id,
			Name: name,
			Hp:   initHP,
		},
	}
}

type Players []*Player

func (players Players) ToPBPlayers() []*pb.Player {
	ret := make([]*pb.Player, len(players))
	for i, p := range players {
		pbp := &pb.Player{
			Id:    p.Id,
			Name:  p.Name,
			Hp:    p.Hp,
			Ready: p.Ready,
		}
		ret[i] = pbp
	}
	return ret
}
