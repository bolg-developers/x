package bolg

type survivalJudgement struct{}

func (sj *survivalJudgement) judge(players Players) (Players, bool) {
	winners := make(Players, 0, len(players))
	for _, p := range players {
		if p.Hp > 0 {
			winners = append(winners, p)
		}
	}
	return winners, len(winners) == 1
}

func newSurivalJudgement() *survivalJudgement {
	return &survivalJudgement{}
}
