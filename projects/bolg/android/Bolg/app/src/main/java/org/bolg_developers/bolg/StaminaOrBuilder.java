// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: server/pb/bolg.proto

package org.bolg_developers.bolg;

public interface StaminaOrBuilder extends
    // @@protoc_insertion_point(interface_extends:bolg.Stamina)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 idx = 1;</code>
   */
  long getIdx();

  /**
   * <code>bool valid = 2;</code>
   */
  boolean getValid();

  /**
   * <code>.google.protobuf.Timestamp use_time = 3;</code>
   */
  boolean hasUseTime();
  /**
   * <code>.google.protobuf.Timestamp use_time = 3;</code>
   */
  com.google.protobuf.Timestamp getUseTime();
  /**
   * <code>.google.protobuf.Timestamp use_time = 3;</code>
   */
  com.google.protobuf.TimestampOrBuilder getUseTimeOrBuilder();
}
