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

  public org.bolg_developers.bolg.RoomMessage.DataCase getDataCase();
}
