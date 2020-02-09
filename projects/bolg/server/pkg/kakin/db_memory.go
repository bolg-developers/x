package kakin

import (
	"fmt"
	"sync"
)

type memoryStaminaDB struct {
	stamins map[string]Stamina
	mu      sync.Mutex
}

func NewMemoryStaminaDB() StaminaDB {
	return &memoryStaminaDB{
		stamins: make(map[string]Stamina, 0),
	}
}

func (db *memoryStaminaDB) Create(s *Stamina) error {
	db.mu.Lock()
	defer db.mu.Unlock()

	if _, ok := db.stamins[s.UserID]; ok {
		return fmt.Errorf("%w with userID %s", ErrStaminaDBAlreadyExists, s.UserID)
	}

	db.stamins[s.UserID] = *s

	return nil
}

func (db *memoryStaminaDB) Get(userID string) (*Stamina, error) {
	db.mu.Lock()
	defer db.mu.Unlock()

	s, ok := db.stamins[userID]
	if !ok {
		return nil, fmt.Errorf("%w with userID %s", ErrStaminaDBNotFound, userID)
	}

	return &s, nil
}

func (db *memoryStaminaDB) Update(s *Stamina) error {
	db.mu.Lock()
	defer db.mu.Unlock()

	_, ok := db.stamins[s.UserID]
	if !ok {
		return fmt.Errorf("%w with userID %s", ErrStaminaDBNotFound, s.UserID)
	}

	db.stamins[s.UserID] = *s

	return nil
}
