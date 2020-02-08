package kakin

import (
	"math"
	"time"

	"github.com/bolg-developers/x/projects/bolg/server/pb"
)

const (
	recoveryTime = 1 * 60 * 60 * 2
)

type Stamina struct {
	UserID       string
	Count        int64
	MaxCount     int64
	RecoveryTime time.Time
}

func (s *Stamina) ToProtoStamina() *pb.Stamina {
	return &pb.Stamina{
		Count:        s.Count,
		MaxCount:     s.MaxCount,
		RecoveryTime: s.RecoveryTime.Unix(),
	}
}

func (s *Stamina) Use() bool {
	if s.Count == 0 {
		return false
	}
	s.Count--

	s.RecoveryTime = s.calcRecoveryTime()
	return true
}

func (s *Stamina) ForceRecovery(n int) {
	for i := 0; i < n; i++ {
		if s.Count == s.MaxCount {
			return
		}
		s.Count++
	}
}

func (s *Stamina) Recovery() {
	cnt := s.countRecovery()
	s.ForceRecovery(cnt)
}

func (s *Stamina) calcRecoveryTime() time.Time {
	return time.Now().Add(time.Duration(1*60*60*2) * time.Second)
}

func (s *Stamina) countRecovery() int {
	now := time.Now()
	d := now.Sub(s.RecoveryTime).Seconds()

	if d < 0 {
		return 0
	}

	if d == 0 {
		s.RecoveryTime = time.Time{}
		return 1
	}

	mod := math.Mod(d, recoveryTime)
	s.RecoveryTime = now.Add(time.Duration(mod) * time.Second)

	cnt := int(d / recoveryTime)

	return cnt
}
