package game

import "github.com/bolg-developers/x/projects/bolg/server/pb"

func Damage(atk, hp int64) int64 {
	out := hp - atk
	if out < 0 {
		out = 0
	}
	return out
}

func IsDead(hp int64) bool {
	return hp == 0
}

func SurvivalJudge(players []Player) (*Player, bool) {
	survs := make([]Player, 0, len(players))
	for _, p := range players {
		if p.Hp != 0 {
			survs = append(survs, p)
		}
	}
	if len(survs) != 1 {
		return nil, false
	}
	return &survs[0], true
}

func CreateSurvivalPersonalResults(players []Player) []*pb.SurvivalPersonalResult {
	out := make([]*pb.SurvivalPersonalResult, 0, len(players))
	for _, p := range players {
		r := &pb.SurvivalPersonalResult{
			PlayerName: p.Name,
			KillCount:  p.KillCnt,
		}
		out = append(out, r)
	}
	return out
}
