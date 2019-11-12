# APIs and Resources
## Summary
APIとリソースの一覧をここに示します。BoLGのAPIについて知りたい人は、まずこのドキュメントでAPIとリソースを概観することをお勧めします。

## APIs
- ルームを作成して入室する
- ルームに参加する
- インベントリーにアイテムを格納する
- ゲーム準備完了を通知する
- ゲームを開始する
- 撃たれたことを通知する
- ゲームの結果を受け取る
- スタミナを消費する
- アイテムを使用する

## Resource Relations
- users
  - staminas
  - inventory
- items
- rooms
  - players
  - gameRule
  - gameResult
    - personalResults