# ルームを作成して入室するAPI

## Method
```proto
service RoomService {
  rpc CreateAndJoinRoom(stream RoomMessage) returns (stream RoomMessage) {}
}
```

## Request
```proto
message CreateAndJoinRoomRequest {
  GameRule game_rule = 1;
}
```

## Response
```proto
message CreateAndJoinRoomResponse {
  Room room = 1;
}
```

## Notes/Constraints
- 単に`ルームを作成する`だけではないのは、ルームをつくって参加するまでの間に悪意のある他のユーザーにハイジャックされる可能性があるから
- 作成されるidは4桁の数字
- ルーム作成者がルームオーナーになる
- ルームは同時にひとつしかつくれない(このロジックはまだ実装しない予定)
- ルームは一定期間以上占有できない(このロジックはまだ実装しない予定)
- `Room Messaging`のエントリーポイント
- HPは初期値100で決まっている
- ユーザー名・攻撃力は`User`リソースから取得可能
