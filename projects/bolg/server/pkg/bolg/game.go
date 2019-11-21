package bolg

import (
	"errors"
	"fmt"
	"sync"
)

var ErrHPisZero = errors.New("receiver or sender hp is zero")

// TODO: こちらもインタフェースはる必要がありそう
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

type judgementManager struct {
	sync.Mutex
	jMap map[int64]judgement
}

func newJudgementManager() *judgementManager {
	return &judgementManager{
		jMap: make(map[int64]judgement, maxRoom),
	}
}

func (jm *judgementManager) create(roomID int64, j judgement) error {
	jm.Lock()
	jm.Unlock()
	_, ok := jm.jMap[roomID]
	if ok {
		return fmt.Errorf("judgement is already exists")
	}
	jm.jMap[roomID] = j
	return nil
}

func (jm *judgementManager) get(roomID int64) (judgement, error) {
	jm.Lock()
	jm.Unlock()
	_, ok := jm.jMap[roomID]
	if !ok {
		return nil, fmt.Errorf("judgement is not found")
	}
	return jm.jMap[roomID], nil
}

// TODO: マネージャーにロジック持たせるのぐあいがわるそう。
func (jm *judgementManager) judge(roomID int64, p Players) (Players, bool, error) {
	j, err := jm.get(roomID)
	if err != nil {
		return nil, false, err
	}
	winners, done := j.judge(p)
	return winners, done, nil
}

type judgement interface {
	// 戻り値のPlayersはゲームの勝者を表し、boolはjudgeがジャッジが終了したどうかを表す。
	judge(Players) (Players, bool)
}
