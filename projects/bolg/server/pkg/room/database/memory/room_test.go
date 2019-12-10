package memory

import (
	"github.com/bolg-developers/x/projects/bolg/server/pb"
	"testing"

	"github.com/bolg-developers/x/projects/bolg/server/pkg/room"
	"github.com/bolg-developers/x/projects/bolg/server/pkg/room/database"
	"github.com/stretchr/testify/assert"
)

func TestRoomDatabase(t *testing.T) {
	db := NewRoomDatabase(10)
	const (
		createRoomID  int64 = 1234
		unknownRoomID int64 = 5678
	)

	r := &room.Room{
		Room: pb.Room{
			Id: createRoomID,
		},
	}
	unknownR := &room.Room{
		Room: pb.Room{
			Id: unknownRoomID,
		},
	}
	immutableRoom := &room.Room{
		Room: pb.Room{
			Id: 4321,
		},
	}

	// ========== Create ==========
	// ok
	err := db.Create(r)
	assert.NoError(t, err)

	// ng
	err = db.Create(r)
	assert.Equal(t, database.ErrAlreadyExists, err)

	// ========== Get ==========
	// ng
	_, err = db.Get(unknownRoomID)
	assert.Equal(t, database.ErrNotFound, err)

	// ok
	gotR, err := db.Get(createRoomID)
	assert.NoError(t, err)
	assert.Equal(t, r, gotR)

	// ========== Update ==========
	// ng
	err = db.Update(unknownR)
	assert.Equal(t, database.ErrNotFound, err)

	// ok
	r.OwnerId = 100
	err = db.Update(r)
	assert.NoError(t, err)
	gotR2, err := db.Get(createRoomID)
	assert.NoError(t, err)
	assert.Equal(t, r, gotR2)

	// ========== Verify Immutable ==========
	err = db.Create(immutableRoom)
	assert.NoError(t, err)
	immutableRoom.OwnerId = 100
	gotR3, err := db.Get(immutableRoom.Id)
	assert.NoError(t, err)
	assert.NotEqual(t, immutableRoom.OwnerId, gotR3.OwnerId)
}
