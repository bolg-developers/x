// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: server/pb/bolg.proto

package org.bolg_developers.bolg;

public interface RecoverHPRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:bolg.RecoverHPRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * hp は回復量を示します。
   * </pre>
   *
   * <code>int64 hp = 1;</code>
   */
  long getHp();

  /**
   * <code>string token = 2;</code>
   */
  java.lang.String getToken();
  /**
   * <code>string token = 2;</code>
   */
  com.google.protobuf.ByteString
      getTokenBytes();
}
