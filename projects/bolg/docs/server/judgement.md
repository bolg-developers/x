# Judgement Design Doc

## Interface
```go
type Judgement interface {
  // Players are winners in game.
  Judge() (Players, error)
}
```

## Example of Using
```go
type Service struct {
  ...
  jm JudgementMap
}

func (svc *Service) handleGamaStart(...) error {
  switch gameType {
  case Survival:
    svc.jm[roomID] = newSurvivalJudgement()
  case TeamSurvival:
    svc.jm[roomID] = newTeamSurvivalJudgement()
  case Unset:
    return errors.New("unset game type")
  default:
    return errors.New("unknow game type")
  }
}

func (svc *Service) handleNotifyReceive() error {
  winners, done := svc.jm[roomID].Judge()
  if !done {
    return ErrNotFinish
  }
  // Reset value of resource
  // Notify game result
}
```
