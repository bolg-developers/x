package kakin

import (
	"math"
	"time"

	"github.com/bolg-developers/x/projects/bolg/server/pb"
)

var (
	now          = func() time.Time { return time.Now() }
	recoveryTime = 1 * 60 * 60 * 2
)

type Stamina struct {
	UserID       string
	Count        int64
	MaxCount     int64
	RecoveryTime time.Time
}

func (s *Stamina) ToProtoStamina() *pb.Stamina {
	var rt int64
	if !s.RecoveryTime.IsZero() {
		rt = s.RecoveryTime.Unix()
	}
	return &pb.Stamina{
		Count:        s.Count,
		MaxCount:     s.MaxCount,
		RecoveryTime: rt,
	}
}

func (s *Stamina) Use() bool {
	if s.Count == 0 {
		return false
	}
	s.Count--
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
	return now().Add(time.Duration(recoveryTime) * time.Second)
}

func (s *Stamina) countRecovery() int {
	n := now()
	d := n.Sub(s.RecoveryTime).Seconds()

	if d < 0 {
		return 0
	}

	cnt := 1
	cnt += int(d / float64(recoveryTime))

	return cnt
}

func (s *Stamina) UpdateRecoveryTime() {
	if s.Count == s.MaxCount {
		s.RecoveryTime = time.Time{}
		return
	}

	n := now()
	d := n.Sub(s.RecoveryTime).Seconds()

	if d < 0 {
		return
	}

	mod := math.Mod(d, float64(recoveryTime))
	s.RecoveryTime = n.Add(time.Duration(mod) * time.Second)
}
