// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: server/pb/bolg.proto

package org.bolg_developers.bolg;

public interface RoomOrBuilder extends
    // @@protoc_insertion_point(interface_extends:bolg.Room)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * output only
   * idはルーム番号にも利用されます。
   * </pre>
   *
   * <code>int64 id = 1;</code>
   */
  long getId();

  /**
   * <pre>
   * output only
   * </pre>
   *
   * <code>.bolg.GameRule game_rule = 2;</code>
   */
  int getGameRuleValue();
  /**
   * <pre>
   * output only
   * </pre>
   *
   * <code>.bolg.GameRule game_rule = 2;</code>
   */
  org.bolg_developers.bolg.GameRule getGameRule();

  /**
   * <pre>
   * output only
   * </pre>
   *
   * <code>repeated .bolg.Player players = 3;</code>
   */
  java.util.List<org.bolg_developers.bolg.Player> 
      getPlayersList();
  /**
   * <pre>
   * output only
   * </pre>
   *
   * <code>repeated .bolg.Player players = 3;</code>
   */
  org.bolg_developers.bolg.Player getPlayers(int index);
  /**
   * <pre>
   * output only
   * </pre>
   *
   * <code>repeated .bolg.Player players = 3;</code>
   */
  int getPlayersCount();
  /**
   * <pre>
   * output only
   * </pre>
   *
   * <code>repeated .bolg.Player players = 3;</code>
   */
  java.util.List<? extends org.bolg_developers.bolg.PlayerOrBuilder> 
      getPlayersOrBuilderList();
  /**
   * <pre>
   * output only
   * </pre>
   *
   * <code>repeated .bolg.Player players = 3;</code>
   */
  org.bolg_developers.bolg.PlayerOrBuilder getPlayersOrBuilder(
      int index);

  /**
   * <pre>
   * output only
   * </pre>
   *
   * <code>bool game_start = 4;</code>
   */
  boolean getGameStart();

  /**
   * <pre>
   * output only
   * owner_idはルームオーナーとなるプレイヤーのIDです。
   * 値が0の場合、オーナーが存在しないことを意味します。
   * </pre>
   *
   * <code>int64 owner_id = 5;</code>
   */
  long getOwnerId();
}
