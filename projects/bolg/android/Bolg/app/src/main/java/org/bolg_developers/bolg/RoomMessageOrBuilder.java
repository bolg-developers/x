// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: server/pb/bolg.proto

package org.bolg_developers.bolg;

public interface RoomMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:bolg.RoomMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.bolg.CreateAndJoinRoomRequest create_and_join_room_req = 1;</code>
   */
  boolean hasCreateAndJoinRoomReq();
  /**
   * <code>.bolg.CreateAndJoinRoomRequest create_and_join_room_req = 1;</code>
   */
  org.bolg_developers.bolg.CreateAndJoinRoomRequest getCreateAndJoinRoomReq();
  /**
   * <code>.bolg.CreateAndJoinRoomRequest create_and_join_room_req = 1;</code>
   */
  org.bolg_developers.bolg.CreateAndJoinRoomRequestOrBuilder getCreateAndJoinRoomReqOrBuilder();

  /**
   * <code>.bolg.CreateAndJoinRoomResponse create_and_join_room_resp = 2;</code>
   */
  boolean hasCreateAndJoinRoomResp();
  /**
   * <code>.bolg.CreateAndJoinRoomResponse create_and_join_room_resp = 2;</code>
   */
  org.bolg_developers.bolg.CreateAndJoinRoomResponse getCreateAndJoinRoomResp();
  /**
   * <code>.bolg.CreateAndJoinRoomResponse create_and_join_room_resp = 2;</code>
   */
  org.bolg_developers.bolg.CreateAndJoinRoomResponseOrBuilder getCreateAndJoinRoomRespOrBuilder();

  /**
   * <code>.bolg.JoinRoomRequest join_room_req = 3;</code>
   */
  boolean hasJoinRoomReq();
  /**
   * <code>.bolg.JoinRoomRequest join_room_req = 3;</code>
   */
  org.bolg_developers.bolg.JoinRoomRequest getJoinRoomReq();
  /**
   * <code>.bolg.JoinRoomRequest join_room_req = 3;</code>
   */
  org.bolg_developers.bolg.JoinRoomRequestOrBuilder getJoinRoomReqOrBuilder();

  /**
   * <code>.bolg.JoinRoomResponse join_room_resp = 4;</code>
   */
  boolean hasJoinRoomResp();
  /**
   * <code>.bolg.JoinRoomResponse join_room_resp = 4;</code>
   */
  org.bolg_developers.bolg.JoinRoomResponse getJoinRoomResp();
  /**
   * <code>.bolg.JoinRoomResponse join_room_resp = 4;</code>
   */
  org.bolg_developers.bolg.JoinRoomResponseOrBuilder getJoinRoomRespOrBuilder();

  /**
   * <code>.bolg.JoinRoomMessage join_room_msg = 5;</code>
   */
  boolean hasJoinRoomMsg();
  /**
   * <code>.bolg.JoinRoomMessage join_room_msg = 5;</code>
   */
  org.bolg_developers.bolg.JoinRoomMessage getJoinRoomMsg();
  /**
   * <code>.bolg.JoinRoomMessage join_room_msg = 5;</code>
   */
  org.bolg_developers.bolg.JoinRoomMessageOrBuilder getJoinRoomMsgOrBuilder();

  /**
   * <code>.bolg.NotifyReceivingRequest notify_receiving_req = 6;</code>
   */
  boolean hasNotifyReceivingReq();
  /**
   * <code>.bolg.NotifyReceivingRequest notify_receiving_req = 6;</code>
   */
  org.bolg_developers.bolg.NotifyReceivingRequest getNotifyReceivingReq();
  /**
   * <code>.bolg.NotifyReceivingRequest notify_receiving_req = 6;</code>
   */
  org.bolg_developers.bolg.NotifyReceivingRequestOrBuilder getNotifyReceivingReqOrBuilder();

  /**
   * <code>.bolg.NotifyReceivingMessage notify_receiving_msg = 7;</code>
   */
  boolean hasNotifyReceivingMsg();
  /**
   * <code>.bolg.NotifyReceivingMessage notify_receiving_msg = 7;</code>
   */
  org.bolg_developers.bolg.NotifyReceivingMessage getNotifyReceivingMsg();
  /**
   * <code>.bolg.NotifyReceivingMessage notify_receiving_msg = 7;</code>
   */
  org.bolg_developers.bolg.NotifyReceivingMessageOrBuilder getNotifyReceivingMsgOrBuilder();

  /**
   * <code>.bolg.SurvivalResultMessage survival_result_msg = 8;</code>
   */
  boolean hasSurvivalResultMsg();
  /**
   * <code>.bolg.SurvivalResultMessage survival_result_msg = 8;</code>
   */
  org.bolg_developers.bolg.SurvivalResultMessage getSurvivalResultMsg();
  /**
   * <code>.bolg.SurvivalResultMessage survival_result_msg = 8;</code>
   */
  org.bolg_developers.bolg.SurvivalResultMessageOrBuilder getSurvivalResultMsgOrBuilder();

  /**
   * <code>.bolg.StartGameRequest start_game_req = 9;</code>
   */
  boolean hasStartGameReq();
  /**
   * <code>.bolg.StartGameRequest start_game_req = 9;</code>
   */
  org.bolg_developers.bolg.StartGameRequest getStartGameReq();
  /**
   * <code>.bolg.StartGameRequest start_game_req = 9;</code>
   */
  org.bolg_developers.bolg.StartGameRequestOrBuilder getStartGameReqOrBuilder();

  /**
   * <code>.bolg.StartGameMessage start_game_msg = 10;</code>
   */
  boolean hasStartGameMsg();
  /**
   * <code>.bolg.StartGameMessage start_game_msg = 10;</code>
   */
  org.bolg_developers.bolg.StartGameMessage getStartGameMsg();
  /**
   * <code>.bolg.StartGameMessage start_game_msg = 10;</code>
   */
  org.bolg_developers.bolg.StartGameMessageOrBuilder getStartGameMsgOrBuilder();

  /**
   * <code>.bolg.UpdateWeaponRequest update_weapon_req = 11;</code>
   */
  boolean hasUpdateWeaponReq();
  /**
   * <code>.bolg.UpdateWeaponRequest update_weapon_req = 11;</code>
   */
  org.bolg_developers.bolg.UpdateWeaponRequest getUpdateWeaponReq();
  /**
   * <code>.bolg.UpdateWeaponRequest update_weapon_req = 11;</code>
   */
  org.bolg_developers.bolg.UpdateWeaponRequestOrBuilder getUpdateWeaponReqOrBuilder();

  /**
   * <code>.bolg.UpdateWeaponResponse update_weapon_resp = 12;</code>
   */
  boolean hasUpdateWeaponResp();
  /**
   * <code>.bolg.UpdateWeaponResponse update_weapon_resp = 12;</code>
   */
  org.bolg_developers.bolg.UpdateWeaponResponse getUpdateWeaponResp();
  /**
   * <code>.bolg.UpdateWeaponResponse update_weapon_resp = 12;</code>
   */
  org.bolg_developers.bolg.UpdateWeaponResponseOrBuilder getUpdateWeaponRespOrBuilder();

  public org.bolg_developers.bolg.RoomMessage.DataCase getDataCase();
}
