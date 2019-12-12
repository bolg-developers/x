package room

import (
	"github.com/bolg-developers/x/projects/bolg/server/pb"
)

const (
	MaxPlayers = 16
	MaxRooms   = 1000 // TODO: 30に戻す
)

type Room struct {
	pb.Room
}

type Player struct {
	pb.Player
}

type Players []*Player

func (players Players) ToGRPCPlayers() []*pb.Player {
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
