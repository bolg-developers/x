// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: server/pb/bolg.proto

package org.bolg_developers.bolg;

/**
 * Protobuf type {@code bolg.NotifyReceivingMessage}
 */
public  final class NotifyReceivingMessage extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:bolg.NotifyReceivingMessage)
    NotifyReceivingMessageOrBuilder {
private static final long serialVersionUID = 0L;
  // Use NotifyReceivingMessage.newBuilder() to construct.
  private NotifyReceivingMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private NotifyReceivingMessage() {
    killerName_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private NotifyReceivingMessage(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            org.bolg_developers.bolg.Player.Builder subBuilder = null;
            if (player_ != null) {
              subBuilder = player_.toBuilder();
            }
            player_ = input.readMessage(org.bolg_developers.bolg.Player.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(player_);
              player_ = subBuilder.buildPartial();
            }

            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            killerName_ = s;
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.bolg_developers.bolg.BolgProto.internal_static_bolg_NotifyReceivingMessage_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.bolg_developers.bolg.BolgProto.internal_static_bolg_NotifyReceivingMessage_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.bolg_developers.bolg.NotifyReceivingMessage.class, org.bolg_developers.bolg.NotifyReceivingMessage.Builder.class);
  }

  public static final int PLAYER_FIELD_NUMBER = 1;
  private org.bolg_developers.bolg.Player player_;
  /**
   * <pre>
   * playerはダメージを受けたプレイヤーです。
   * </pre>
   *
   * <code>.bolg.Player player = 1;</code>
   */
  public boolean hasPlayer() {
    return player_ != null;
  }
  /**
   * <pre>
   * playerはダメージを受けたプレイヤーです。
   * </pre>
   *
   * <code>.bolg.Player player = 1;</code>
   */
  public org.bolg_developers.bolg.Player getPlayer() {
    return player_ == null ? org.bolg_developers.bolg.Player.getDefaultInstance() : player_;
  }
  /**
   * <pre>
   * playerはダメージを受けたプレイヤーです。
   * </pre>
   *
   * <code>.bolg.Player player = 1;</code>
   */
  public org.bolg_developers.bolg.PlayerOrBuilder getPlayerOrBuilder() {
    return getPlayer();
  }

  public static final int KILLERNAME_FIELD_NUMBER = 2;
  private volatile java.lang.Object killerName_;
  /**
   * <pre>
   * killerNameはダメージを受けたプレイヤーを殺したプレイヤーの名前です。
   * ダメージを受けたプレイヤーが死亡していない場合、このフィールドは空文字となります。
   * </pre>
   *
   * <code>string killerName = 2;</code>
   */
  public java.lang.String getKillerName() {
    java.lang.Object ref = killerName_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      killerName_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * killerNameはダメージを受けたプレイヤーを殺したプレイヤーの名前です。
   * ダメージを受けたプレイヤーが死亡していない場合、このフィールドは空文字となります。
   * </pre>
   *
   * <code>string killerName = 2;</code>
   */
  public com.google.protobuf.ByteString
      getKillerNameBytes() {
    java.lang.Object ref = killerName_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      killerName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (player_ != null) {
      output.writeMessage(1, getPlayer());
    }
    if (!getKillerNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, killerName_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (player_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getPlayer());
    }
    if (!getKillerNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, killerName_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof org.bolg_developers.bolg.NotifyReceivingMessage)) {
      return super.equals(obj);
    }
    org.bolg_developers.bolg.NotifyReceivingMessage other = (org.bolg_developers.bolg.NotifyReceivingMessage) obj;

    boolean result = true;
    result = result && (hasPlayer() == other.hasPlayer());
    if (hasPlayer()) {
      result = result && getPlayer()
          .equals(other.getPlayer());
    }
    result = result && getKillerName()
        .equals(other.getKillerName());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasPlayer()) {
      hash = (37 * hash) + PLAYER_FIELD_NUMBER;
      hash = (53 * hash) + getPlayer().hashCode();
    }
    hash = (37 * hash) + KILLERNAME_FIELD_NUMBER;
    hash = (53 * hash) + getKillerName().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.bolg_developers.bolg.NotifyReceivingMessage parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bolg_developers.bolg.NotifyReceivingMessage parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bolg_developers.bolg.NotifyReceivingMessage parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bolg_developers.bolg.NotifyReceivingMessage parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bolg_developers.bolg.NotifyReceivingMessage parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bolg_developers.bolg.NotifyReceivingMessage parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bolg_developers.bolg.NotifyReceivingMessage parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.bolg_developers.bolg.NotifyReceivingMessage parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.bolg_developers.bolg.NotifyReceivingMessage parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.bolg_developers.bolg.NotifyReceivingMessage parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.bolg_developers.bolg.NotifyReceivingMessage parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.bolg_developers.bolg.NotifyReceivingMessage parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(org.bolg_developers.bolg.NotifyReceivingMessage prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code bolg.NotifyReceivingMessage}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:bolg.NotifyReceivingMessage)
      org.bolg_developers.bolg.NotifyReceivingMessageOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.bolg_developers.bolg.BolgProto.internal_static_bolg_NotifyReceivingMessage_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.bolg_developers.bolg.BolgProto.internal_static_bolg_NotifyReceivingMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.bolg_developers.bolg.NotifyReceivingMessage.class, org.bolg_developers.bolg.NotifyReceivingMessage.Builder.class);
    }

    // Construct using org.bolg_developers.bolg.NotifyReceivingMessage.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      if (playerBuilder_ == null) {
        player_ = null;
      } else {
        player_ = null;
        playerBuilder_ = null;
      }
      killerName_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.bolg_developers.bolg.BolgProto.internal_static_bolg_NotifyReceivingMessage_descriptor;
    }

    public org.bolg_developers.bolg.NotifyReceivingMessage getDefaultInstanceForType() {
      return org.bolg_developers.bolg.NotifyReceivingMessage.getDefaultInstance();
    }

    public org.bolg_developers.bolg.NotifyReceivingMessage build() {
      org.bolg_developers.bolg.NotifyReceivingMessage result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public org.bolg_developers.bolg.NotifyReceivingMessage buildPartial() {
      org.bolg_developers.bolg.NotifyReceivingMessage result = new org.bolg_developers.bolg.NotifyReceivingMessage(this);
      if (playerBuilder_ == null) {
        result.player_ = player_;
      } else {
        result.player_ = playerBuilder_.build();
      }
      result.killerName_ = killerName_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.bolg_developers.bolg.NotifyReceivingMessage) {
        return mergeFrom((org.bolg_developers.bolg.NotifyReceivingMessage)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.bolg_developers.bolg.NotifyReceivingMessage other) {
      if (other == org.bolg_developers.bolg.NotifyReceivingMessage.getDefaultInstance()) return this;
      if (other.hasPlayer()) {
        mergePlayer(other.getPlayer());
      }
      if (!other.getKillerName().isEmpty()) {
        killerName_ = other.killerName_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      org.bolg_developers.bolg.NotifyReceivingMessage parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.bolg_developers.bolg.NotifyReceivingMessage) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private org.bolg_developers.bolg.Player player_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.bolg_developers.bolg.Player, org.bolg_developers.bolg.Player.Builder, org.bolg_developers.bolg.PlayerOrBuilder> playerBuilder_;
    /**
     * <pre>
     * playerはダメージを受けたプレイヤーです。
     * </pre>
     *
     * <code>.bolg.Player player = 1;</code>
     */
    public boolean hasPlayer() {
      return playerBuilder_ != null || player_ != null;
    }
    /**
     * <pre>
     * playerはダメージを受けたプレイヤーです。
     * </pre>
     *
     * <code>.bolg.Player player = 1;</code>
     */
    public org.bolg_developers.bolg.Player getPlayer() {
      if (playerBuilder_ == null) {
        return player_ == null ? org.bolg_developers.bolg.Player.getDefaultInstance() : player_;
      } else {
        return playerBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     * playerはダメージを受けたプレイヤーです。
     * </pre>
     *
     * <code>.bolg.Player player = 1;</code>
     */
    public Builder setPlayer(org.bolg_developers.bolg.Player value) {
      if (playerBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        player_ = value;
        onChanged();
      } else {
        playerBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <pre>
     * playerはダメージを受けたプレイヤーです。
     * </pre>
     *
     * <code>.bolg.Player player = 1;</code>
     */
    public Builder setPlayer(
        org.bolg_developers.bolg.Player.Builder builderForValue) {
      if (playerBuilder_ == null) {
        player_ = builderForValue.build();
        onChanged();
      } else {
        playerBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <pre>
     * playerはダメージを受けたプレイヤーです。
     * </pre>
     *
     * <code>.bolg.Player player = 1;</code>
     */
    public Builder mergePlayer(org.bolg_developers.bolg.Player value) {
      if (playerBuilder_ == null) {
        if (player_ != null) {
          player_ =
            org.bolg_developers.bolg.Player.newBuilder(player_).mergeFrom(value).buildPartial();
        } else {
          player_ = value;
        }
        onChanged();
      } else {
        playerBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <pre>
     * playerはダメージを受けたプレイヤーです。
     * </pre>
     *
     * <code>.bolg.Player player = 1;</code>
     */
    public Builder clearPlayer() {
      if (playerBuilder_ == null) {
        player_ = null;
        onChanged();
      } else {
        player_ = null;
        playerBuilder_ = null;
      }

      return this;
    }
    /**
     * <pre>
     * playerはダメージを受けたプレイヤーです。
     * </pre>
     *
     * <code>.bolg.Player player = 1;</code>
     */
    public org.bolg_developers.bolg.Player.Builder getPlayerBuilder() {
      
      onChanged();
      return getPlayerFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     * playerはダメージを受けたプレイヤーです。
     * </pre>
     *
     * <code>.bolg.Player player = 1;</code>
     */
    public org.bolg_developers.bolg.PlayerOrBuilder getPlayerOrBuilder() {
      if (playerBuilder_ != null) {
        return playerBuilder_.getMessageOrBuilder();
      } else {
        return player_ == null ?
            org.bolg_developers.bolg.Player.getDefaultInstance() : player_;
      }
    }
    /**
     * <pre>
     * playerはダメージを受けたプレイヤーです。
     * </pre>
     *
     * <code>.bolg.Player player = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.bolg_developers.bolg.Player, org.bolg_developers.bolg.Player.Builder, org.bolg_developers.bolg.PlayerOrBuilder> 
        getPlayerFieldBuilder() {
      if (playerBuilder_ == null) {
        playerBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.bolg_developers.bolg.Player, org.bolg_developers.bolg.Player.Builder, org.bolg_developers.bolg.PlayerOrBuilder>(
                getPlayer(),
                getParentForChildren(),
                isClean());
        player_ = null;
      }
      return playerBuilder_;
    }

    private java.lang.Object killerName_ = "";
    /**
     * <pre>
     * killerNameはダメージを受けたプレイヤーを殺したプレイヤーの名前です。
     * ダメージを受けたプレイヤーが死亡していない場合、このフィールドは空文字となります。
     * </pre>
     *
     * <code>string killerName = 2;</code>
     */
    public java.lang.String getKillerName() {
      java.lang.Object ref = killerName_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        killerName_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * killerNameはダメージを受けたプレイヤーを殺したプレイヤーの名前です。
     * ダメージを受けたプレイヤーが死亡していない場合、このフィールドは空文字となります。
     * </pre>
     *
     * <code>string killerName = 2;</code>
     */
    public com.google.protobuf.ByteString
        getKillerNameBytes() {
      java.lang.Object ref = killerName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        killerName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * killerNameはダメージを受けたプレイヤーを殺したプレイヤーの名前です。
     * ダメージを受けたプレイヤーが死亡していない場合、このフィールドは空文字となります。
     * </pre>
     *
     * <code>string killerName = 2;</code>
     */
    public Builder setKillerName(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      killerName_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * killerNameはダメージを受けたプレイヤーを殺したプレイヤーの名前です。
     * ダメージを受けたプレイヤーが死亡していない場合、このフィールドは空文字となります。
     * </pre>
     *
     * <code>string killerName = 2;</code>
     */
    public Builder clearKillerName() {
      
      killerName_ = getDefaultInstance().getKillerName();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * killerNameはダメージを受けたプレイヤーを殺したプレイヤーの名前です。
     * ダメージを受けたプレイヤーが死亡していない場合、このフィールドは空文字となります。
     * </pre>
     *
     * <code>string killerName = 2;</code>
     */
    public Builder setKillerNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      killerName_ = value;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:bolg.NotifyReceivingMessage)
  }

  // @@protoc_insertion_point(class_scope:bolg.NotifyReceivingMessage)
  private static final org.bolg_developers.bolg.NotifyReceivingMessage DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.bolg_developers.bolg.NotifyReceivingMessage();
  }

  public static org.bolg_developers.bolg.NotifyReceivingMessage getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<NotifyReceivingMessage>
      PARSER = new com.google.protobuf.AbstractParser<NotifyReceivingMessage>() {
    public NotifyReceivingMessage parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new NotifyReceivingMessage(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<NotifyReceivingMessage> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<NotifyReceivingMessage> getParserForType() {
    return PARSER;
  }

  public org.bolg_developers.bolg.NotifyReceivingMessage getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

