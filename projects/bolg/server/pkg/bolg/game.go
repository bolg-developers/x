package bolg

import (
	"errors"
)

var ErrHPisZero = errors.New("receiver or sender hp is zero")

func damage(receiver, sender *Player) (bool, error) {
	if receiver.Hp == 0 {
		return false, ErrHPisZero
	}
	if sender.Hp == 0 {
		return false, ErrHPisZero
	}
	receiver.Hp -= sender.Attack
	if receiver.Hp < 0 {
		receiver.Hp = 0
		sender.Kill++
	}
	return receiver.Hp == 0, nil
}
