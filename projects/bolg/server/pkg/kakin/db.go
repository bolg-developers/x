package kakin

import "errors"

var (
	ErrStaminaDBAlreadyExists = errors.New("staminadb: already exists stamina")
	ErrStaminaDBNotFound      = errors.New("staminadb: not found stamina")
)

type StaminaDB interface {
	Get(userID string) (*Stamina, error)
	Create(*Stamina) error
	Update(*Stamina) error
}

func InjectStamins(db StaminaDB) error {
	stamins := []*Stamina{
		{
			UserID:   "matsumura",
			Count:    3,
			MaxCount: 3,
		},
		{
			UserID:   "takeuchi",
			Count:    3,
			MaxCount: 3,
		},
		{
			UserID:   "yamaguchi",
			Count:    3,
			MaxCount: 3,
		},
		{
			UserID:   "hasegawa",
			Count:    3,
			MaxCount: 3,
		},
		{
			UserID:   "nakata",
			Count:    3,
			MaxCount: 3,
		},
		{
			UserID:   "sasaki",
			Count:    3,
			MaxCount: 3,
		},
	}
	for _, s := range stamins {
		if err := db.Create(s); err != nil {
			return err
		}
	}
	return nil
}
