package game

import (
	"errors"
	"fmt"

	"github.com/bolg-developers/x/projects/bolg/server/pb"
)

const (
	maxPlayer       = 16
	defaultHP int64 = 100
)

type Room struct {
	ID        int64
	Players   []Player
	Rule      pb.GameRule
	GameStart bool
	OwnerID   int64
	alloc     *playerIDAllocator
}

func NewRoom() *Room {
	return &Room{
		Players: make([]Player, 0, maxPlayer),
		Rule:    pb.GameRule_SURVIVAL,
		alloc:   newPlayerIDAllocator(),
	}
}

func (r *Room) ToProtoRoom() *pb.Room {
	players := make([]*pb.Player, len(r.Players))
	for i, p := range r.Players {
		players[i] = p.ToProtoPlayer()
	}
	return &pb.Room{
		Id:        r.ID,
		GameRule:  r.Rule,
		GameStart: r.GameStart,
		OwnerId:   r.OwnerID,
		Players:   players,
	}
}

func (r *Room) Broadcast(f func(*Player) bool, msg *pb.RoomMessage) []error {
	errs := make([]error, 0, len(r.Players))
	for _, p := range r.Players {
		if f(&p) {
			if err := p.Stream.Send(msg); err != nil {
				errs = append(errs, fmt.Errorf("%s(roomID,playerID)=(%d,%d)", err.Error(), r.ID, p.ID))
			}
		}
	}
	return errs
}

func FilterNothing() func(*Player) bool {
	return func(*Player) bool { return true }
}

func FilterNotByPlayerID(id int64) func(*Player) bool {
	return func(p *Player) bool {
		return p.ID != id
	}
}

func (r *Room) AddPlayer(p *Player) error {
	id, err := r.alloc.allocate()
	if err != nil {
		return err
	}
	p.ID = id
	r.Players = append(r.Players, *p)
	return nil
}

func (r *Room) GetPlayer(id int64) (*Player, error) {
	for _, p := range r.Players {
		if p.ID == id {
			return &p, nil
		}
	}
	return nil, fmt.Errorf("not found player with ID %d", id)
}

func (r *Room) UpdatePlayer(p *Player) error {
	for i, pl := range r.Players {
		if pl.ID == p.ID {
			r.Players[i] = *p
			return nil
		}
	}
	return fmt.Errorf("not found player with ID %d", p.ID)
}

func (r *Room) ResetHP() {
	for i := range r.Players {
		r.Players[i].Hp = defaultHP
	}
}

func (r *Room) initPlayers() {
	for i := range r.Players {
		r.Players[i].Hp = defaultHP
		r.Players[i].Ready = false
		r.Players[i].KillCnt = 0
	}
}

type Player struct {
	ID      int64
	Name    string
	Hp      int64
	Power   int64
	Ready   bool
	KillCnt int64
	Stream  pb.BolgService_ConnectServer
}

func (p *Player) ToProtoPlayer() *pb.Player {
	return &pb.Player{
		Id:    p.ID,
		Name:  p.Name,
		Hp:    p.Hp,
		Ready: p.Ready,
	}
}

func NewPlayer(name string) *Player {
	return &Player{
		Name: name,
		Hp:   defaultHP,
	}
}

type playerIDAllocator struct {
	ids map[int64]bool
}

func newPlayerIDAllocator() *playerIDAllocator {
	ids := make(map[int64]bool, maxPlayer)
	var id int64
	for id = 0; id < maxPlayer; id++ {
		ids[id] = false
	}
	return &playerIDAllocator{
		ids: ids,
	}
}

func (a *playerIDAllocator) allocate() (int64, error) {
	for id, used := range a.ids {
		if !used {
			a.ids[id] = true
			return id, nil
		}
	}
	return 0, errors.New("all player id used")
}
