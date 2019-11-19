// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: server/pb/bolg.proto

package org.bolg_developers.bolg;

public interface NotifyReceivingRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:bolg.NotifyReceivingRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * player_idは受信したプレイヤーIDです。
   * </pre>
   *
   * <code>int64 player_id = 1;</code>
   */
  long getPlayerId();

  /**
   * <pre>
   * tokenはクライアントがどのルームの誰なのかを表します。
   * ルームを入室したときに生成されたtokenをセットしてください。
   * </pre>
   *
   * <code>string token = 2;</code>
   */
  java.lang.String getToken();
  /**
   * <pre>
   * tokenはクライアントがどのルームの誰なのかを表します。
   * ルームを入室したときに生成されたtokenをセットしてください。
   * </pre>
   *
   * <code>string token = 2;</code>
   */
  com.google.protobuf.ByteString
      getTokenBytes();
}
