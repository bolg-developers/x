package room

import (
	"testing"

	"github.com/stretchr/testify/require"
)

func TestPlayerIDManager(t *testing.T) {
	pidm := NewPlayerIDManager(1)

	// test get: ok
	id, err := pidm.Get()
	require.NoError(t, err)
	require.Equal(t, int64(0), id)

	// test get: scarcity of capacity error
	_, err = pidm.Get()
	require.Error(t, err)

	// test delete: invalid id
	err = pidm.Delete(id + 1)
	require.Error(t, err)

	// test delete: ok
	err = pidm.Delete(id)
	require.NoError(t, err)
}
