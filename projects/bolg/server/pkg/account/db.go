package account

import "errors"

var (
	ErrAccountDBAlreadyExists = errors.New("accountdb: already exists account")
	ErrAccountDBNotFound      = errors.New("accountdb: not found account")
)

type AccountDB interface {
	Create(*Account) error
	Get(string) (*Account, error)
}

func InjectAccounts(db AccountDB) error {
	const password = "hal-osaka"
	accounts := []*Account{
		{
			ID:       "matsumura",
			Password: password,
		},
		{
			ID:       "takeuchi",
			Password: password,
		},
		{
			ID:       "yamaguchi",
			Password: password,
		},
		{
			ID:       "nakata",
			Password: password,
		},
		{
			ID:       "hasegawa",
			Password: password,
		},
		{
			ID:       "sasaki",
			Password: password,
		},
	}
	for _, a := range accounts {
		if err := db.Create(a); err != nil {
			return err
		}
	}
	return nil
}
