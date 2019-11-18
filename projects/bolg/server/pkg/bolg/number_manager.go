package bolg

import (
	"math/rand"
	"sync"
	"time"
)

const emptyNum = -1

type numberManager struct {
	sync.Mutex
	nums []int64
}

func init() {
	rand.Seed(time.Now().UnixNano())
}

func newNumberManager() *numberManager {
	nums := make([]int64, maxRoom)
	for i := range nums {
		nums[i] = emptyNum
	}
	return &numberManager{
		nums: nums,
	}
}

func (nm *numberManager) createNum() (int64, bool) {
	nm.Lock()
	defer nm.Unlock()
	idx, found := nm.searchEmptyNumIdx()
	if !found {
		return 0, false
	}

	// 現在、ルームの最大数が30程度を想定しているのでこのアルゴリズムを採用する。
	// 数値が衝突する確率は最大の場合でも29/10000*100=0.29%
	// 程度なので問題ないと考えられる。
	// しかし最大ルーム数が増えるにつれて、ルーム番号も衝突しやすくなる。
	// その場合は別のアルゴリズムを考える。
	var ret int64
	for {
		ret = rand.Int63n(10000)
		if !nm.contains(ret) {
			break
		}
	}
	nm.nums[idx] = ret
	return ret, true
}

func (nm *numberManager) searchEmptyNumIdx() (int, bool) {
	for i, n := range nm.nums {
		if n == emptyNum {
			return i, true
		}
	}
	return 0, false
}

func (nm *numberManager) contains(n int64) bool {
	for _, num := range nm.nums {
		if num == n {
			return true
		}
	}
	return false
}
