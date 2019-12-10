package memory

import (
	"github.com/bolg-developers/x/projects/bolg/server/pkg/room/database"
	"testing"

	"github.com/bolg-developers/x/projects/bolg/server/pb"
	"github.com/bolg-developers/x/projects/bolg/server/pkg/room"
	"github.com/stretchr/testify/assert"
)

func TestPlayerDatabase(t *testing.T) {
	db := NewPlayerDatabase(16)
	const (
		roomID = 1234
	)
	fakePlayer1 := &room.Player{
		Player: pb.Player{
			Id:   1,
			Name: "masui",
		},
	}
	fakePlayer2 := &room.Player{
		Player: pb.Player{
			Id: 2,
		},
	}
	fakePlayer3 := &room.Player{
		Player: pb.Player{
			Id: 3,
		},
	}
	immutablePlayer := &room.Player{
		Player: pb.Player{
			Id:   4,
			Name: "hoge",
		},
	}

	// ********** Create **********
	// ok
	err := db.Create(roomID, fakePlayer1)
	assert.NoError(t, err)
	// ng: ErrAlreadyExists
	err = db.Create(roomID, fakePlayer1)
	assert.Equal(t, database.ErrAlreadyExists, err)

	// ********** Get **********
	// ok
	p, err := db.Get(roomID, fakePlayer1.Id)
	assert.NoError(t, err)
	assert.Equal(t, fakePlayer1, p)
	// ng: not found
	_, err = db.Get(roomID, fakePlayer2.Id)
	assert.Equal(t, database.ErrNotFound, err)

	// ********** List **********
	// ok
	err = db.Create(roomID, fakePlayer2)
	assert.NoError(t, err)
	players, err := db.List(roomID)
	assert.NoError(t, err)
	assert.Equal(t, room.Players{fakePlayer1, fakePlayer2}, players)

	// ********** Update **********
	// ok
	fakePlayer1.Name = "hara"
	err = db.Update(roomID, fakePlayer1)
	assert.NoError(t, err)
	p2, err := db.Get(roomID, fakePlayer1.Id)
	assert.NoError(t, err)
	assert.Equal(t, fakePlayer1, p2)
	// ng: not found
	err = db.Update(roomID, fakePlayer3)
	assert.Equal(t, database.ErrNotFound, err)

	// ********** Verify Immutable **********
	err = db.Create(roomID, immutablePlayer)
	assert.NoError(t, err)
	immutablePlayer.Name = "piyo"
	p3, err := db.Get(roomID, immutablePlayer.Id)
	assert.NoError(t, err)
	assert.NotEqual(t, immutablePlayer.Name, p3.Name)
}
