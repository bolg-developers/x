package game

import (
	"testing"

	"github.com/stretchr/testify/require"
)

func TestRoomDB(t *testing.T) {
	db := NewMemoryRoomDB()

	const (
		id   int64 = 0
		ngid int64 = 9999
	)
	r := &Room{
		ID: id,
	}

	// create ok
	err := db.Create(r)
	require.NoError(t, err)

	// create ng
	err = db.Create(r)
	require.Error(t, err)

	// get ok
	got, err := db.Get(id)
	require.NoError(t, err)
	require.Equal(t, r, got)

	// get ng
	_, err = db.Get(ngid)
	require.Error(t, err)

	// update ok
	r.GameStart = true
	err = db.Update(r)
	require.NoError(t, err)

	// update ng
	r.ID = ngid
	err = db.Update(r)
	require.Error(t, err)
}
