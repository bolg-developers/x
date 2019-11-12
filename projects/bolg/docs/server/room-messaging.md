# Room Messaging
## Note
BoLGのクライアントとサーバー間では高速な通信が一部では要求されるためBidirectional Streamingを用いた双方向通信を行います。BoLG APIではその通信を`Room Messaging`と呼ぶことにします。

## Protocol Buffers
```proto
message RoomMessage {
  oneof data {
    CreateAndJoinRoomRequest create_and_join_room_req = 1;
    CreateAndJoinRoomResponse create_and_join_room_resp = 2;
  }
}

message CreateAndJoinRoomRequest {}
message CreateAndJoinRoomResponse {}
```
## エントリーポイントとなるAPI
- CreateAndJoinRoom
- JoinRoom

## Room Service
```go
type RoomService struct {}

func (r *RoomService) CreateAndJoinRoom(stream Stream) error {
  if err := r.handleRoomMessage(stream); err != nil {
    // ルームオーナーが退室したときのロジック
  }
}

func (r *RoomService) JoinRoom(stream Stream) error {
  return r.handleRoomMessage(stream)
}

func (r *RoomService) handleRoomMessage(stream Stream) error {
  for {
    in, err := stream.Recv()
    if err == io.EOF {
      return nil
    }
    if err != nil {
      return err
    }
    user, err := GetCurrentUser()
    if err != nil {
      return err
    }
    switch in.(type) {
      // inのタイプに応じたロジック
    }
  }
}
```

## MessageのBroadcast
今の所、再送のロジックは考えていません。あまりにもメッセージエラーが発生するようなら考えます。
```go
type RoomService struct {
  // ...
  streamMap map[int64][]*Stream
}
func (r *RoomService) broadcast(roomID int64, msg *pb.RoomMessage) {
  for _, stream := range r.streamMap[roomID] {
    if err := stream.Send(msg); err != nil {
      // logging
    }
  }
}
```

## MessageのQueuing
今回つくるのはあくまでもプロトタイプなので、大規模なアクセスは想定していません。そのため、MessageのQueuingも実装する予定はありません。