package room

import (
	"errors"
	"math/rand"
	"sync"
	"time"
)

const empty int64 = -1

type IDManager struct {
	sync.Mutex
	ids []int64
}

func NewIDManager(size int) *IDManager {
	ids := make([]int64, size)
	for i := range ids {
		ids[i] = empty
	}
	return &IDManager{
		ids: ids,
	}
}

func (idm *IDManager) Create() (int64, error) {
	idm.Lock()
	defer idm.Unlock()
	idx, err := idm.searchIdxEmpty()
	if err != nil {
		return empty, err
	}

	// 現在、ルームの最大数が30程度を想定しているのでこのアルゴリズムを採用する。
	// 数値が衝突する確率は最大の場合でも29/10000*100=0.29%
	// 程度なので問題ないと考えられる。
	// しかし最大ルーム数が増えるにつれて、ルーム番号も衝突しやすくなる。
	// その場合は別のアルゴリズムを考える。
	var ret int64
	for {
		ret = rand.Int63n(10000)
		if _, ok := idm.contains(ret); !ok {
			break
		}
	}
	idm.ids[idx] = ret
	return ret, nil
}

func (idm *IDManager) Delete(id int64) error {
	idm.Lock()
	defer idm.Unlock()
	idx, ok := idm.contains(id)
	if !ok {
		return errors.New("room id manager: not found id")
	}
	idm.ids[idx] = empty
	return nil
}

func (idm *IDManager) searchIdxEmpty() (int, error) {
	for i, id := range idm.ids {
		if id == empty {
			return i, nil
		}
	}
	return 0, errors.New("room id manager: not found index of empty")
}

func (idm *IDManager) contains(id int64) (int, bool) {
	for i, ide := range idm.ids {
		if ide == id {
			return i, true
		}
	}
	return 0, false
}

func init() {
	rand.Seed(time.Now().UnixNano())
}
