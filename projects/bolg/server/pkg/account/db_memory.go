package account

import (
	"fmt"
	"sync"
)

type memoryAccountDB struct {
	accounts map[string]Account
	mu       sync.Mutex
}

func NewMemoryAccountDB() AccountDB {
	return &memoryAccountDB{
		accounts: make(map[string]Account, 0),
	}
}

func (db *memoryAccountDB) Create(a *Account) error {
	db.mu.Lock()
	defer db.mu.Unlock()

	_, ok := db.accounts[a.ID]
	if ok {
		return fmt.Errorf("%w with ID %s", ErrAccountDBAlreadyExists, a.ID)
	}
	db.accounts[a.ID] = *a

	return nil
}

func (db *memoryAccountDB) Get(id string) (*Account, error) {
	db.mu.Lock()
	defer db.mu.Unlock()

	a, ok := db.accounts[id]
	if !ok {
		return nil, fmt.Errorf("%w with ID %s", ErrAccountDBNotFound, id)
	}
	return &a, nil
}
