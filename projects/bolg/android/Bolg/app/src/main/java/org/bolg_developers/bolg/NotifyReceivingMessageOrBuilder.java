// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: server/pb/bolg.proto

package org.bolg_developers.bolg;

public interface NotifyReceivingMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:bolg.NotifyReceivingMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * playerはダメージを受けたプレイヤーです。
   * </pre>
   *
   * <code>.bolg.Player player = 1;</code>
   */
  boolean hasPlayer();
  /**
   * <pre>
   * playerはダメージを受けたプレイヤーです。
   * </pre>
   *
   * <code>.bolg.Player player = 1;</code>
   */
  org.bolg_developers.bolg.Player getPlayer();
  /**
   * <pre>
   * playerはダメージを受けたプレイヤーです。
   * </pre>
   *
   * <code>.bolg.Player player = 1;</code>
   */
  org.bolg_developers.bolg.PlayerOrBuilder getPlayerOrBuilder();

  /**
   * <pre>
   * killerNameはダメージを受けたプレイヤーを殺したプレイヤーの名前です。
   * ダメージを受けたプレイヤーが死亡していない場合、このフィールドは空文字となります。
   * </pre>
   *
   * <code>string killerName = 2;</code>
   */
  java.lang.String getKillerName();
  /**
   * <pre>
   * killerNameはダメージを受けたプレイヤーを殺したプレイヤーの名前です。
   * ダメージを受けたプレイヤーが死亡していない場合、このフィールドは空文字となります。
   * </pre>
   *
   * <code>string killerName = 2;</code>
   */
  com.google.protobuf.ByteString
      getKillerNameBytes();
}
