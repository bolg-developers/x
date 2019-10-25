# Design Doc

WARNING: THIS IS DRAFT.

## Client Use Caces 
- ルームを作成する
- ルームに参加する
- バトルを開始する
- バトルを中断する
- 撃たれたことを通知
- ルームを削除する

## Server Use Caces
- バトルの結果を通知する

## Resouces 
- room
- player
- battleResult

## Resource Relations
- rooms
  - players
  - battleResult

## Resource Schemes
- room
  - id
  - roomOwnerId
  - fighting

## Methods
- CreateRoom
- JoinRoom
- StartBattle
- StopBattle
- NotifyReceiving
- DeleteRoom

## Non Methods