// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: server/pb/bolg.proto

package org.bolg_developers.bolg;

public interface JoinRoomMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:bolg.JoinRoomMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * playerは入室してきたプレイヤーです。
   * </pre>
   *
   * <code>.bolg.Player player = 1;</code>
   */
  boolean hasPlayer();
  /**
   * <pre>
   * playerは入室してきたプレイヤーです。
   * </pre>
   *
   * <code>.bolg.Player player = 1;</code>
   */
  org.bolg_developers.bolg.Player getPlayer();
  /**
   * <pre>
   * playerは入室してきたプレイヤーです。
   * </pre>
   *
   * <code>.bolg.Player player = 1;</code>
   */
  org.bolg_developers.bolg.PlayerOrBuilder getPlayerOrBuilder();
}
