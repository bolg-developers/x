package bolg

import (
	"github.com/bolg-developers/x/projects/bolg/server/pb"
)

const (
	maxPlayer = 16
	maxRoom   = 1000
	initHP    = 100
)

type Room struct {
	pb.Room
}

func NewRoom(id int64) *Room {
	return &Room{
		Room: pb.Room{
			Id:       id,
			GameRule: pb.GameRule_SURVIVAL,
		},
	}
}

type Player struct {
	pb.Player
	Attack int64
	Kill   int64
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

func (players Players) ToSurivalPersonalResults() []*pb.SurvivalPersonalResult {
	results := make([]*pb.SurvivalPersonalResult, len(players))
	for i, p := range players {
		results[i] = &pb.SurvivalPersonalResult{
			PlayerName: p.Name,
			KillCount:  p.Kill,
		}
	}
	return results
}

func (players Players) NotReadyPlayers() Players {
	ret := make(Players, 0, len(players))
	for _, p := range players {
		if !p.Ready {
			ret = append(ret, p)
		}
	}
	return ret
}

func changeReadyFalse(players Players) {
	for _, p := range players {
		p.Ready = false
	}
}
