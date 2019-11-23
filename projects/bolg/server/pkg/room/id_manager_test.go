package room

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestIDManager(t *testing.T) {
	idm := NewIDManager(1)

	// test create: ok
	id, err := idm.Create()
	require.NoError(t, err)
	require.NotEqual(t, empty, id)

	// test create: scarcity of capacity error
	wantEmptyID, err := idm.Create()
	require.Error(t, err)
	require.Equal(t, empty, wantEmptyID)

	// test delete: invalid id
	err = idm.Delete(id + 1)
	assert.Error(t, err)

	// test delete: ok
	err = idm.Delete(id)
	assert.NoError(t, err)
}
