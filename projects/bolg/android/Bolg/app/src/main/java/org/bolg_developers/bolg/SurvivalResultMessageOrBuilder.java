// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: server/pb/bolg.proto

package org.bolg_developers.bolg;

public interface SurvivalResultMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:bolg.SurvivalResultMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.bolg.Player winner = 1;</code>
   */
  boolean hasWinner();
  /**
   * <code>.bolg.Player winner = 1;</code>
   */
  org.bolg_developers.bolg.Player getWinner();
  /**
   * <code>.bolg.Player winner = 1;</code>
   */
  org.bolg_developers.bolg.PlayerOrBuilder getWinnerOrBuilder();

  /**
   * <code>repeated .bolg.SurvivalPersonalResult personals = 2;</code>
   */
  java.util.List<org.bolg_developers.bolg.SurvivalPersonalResult> 
      getPersonalsList();
  /**
   * <code>repeated .bolg.SurvivalPersonalResult personals = 2;</code>
   */
  org.bolg_developers.bolg.SurvivalPersonalResult getPersonals(int index);
  /**
   * <code>repeated .bolg.SurvivalPersonalResult personals = 2;</code>
   */
  int getPersonalsCount();
  /**
   * <code>repeated .bolg.SurvivalPersonalResult personals = 2;</code>
   */
  java.util.List<? extends org.bolg_developers.bolg.SurvivalPersonalResultOrBuilder> 
      getPersonalsOrBuilderList();
  /**
   * <code>repeated .bolg.SurvivalPersonalResult personals = 2;</code>
   */
  org.bolg_developers.bolg.SurvivalPersonalResultOrBuilder getPersonalsOrBuilder(
      int index);
}
