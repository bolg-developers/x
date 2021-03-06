// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: server/pb/bolg.proto

package org.bolg_developers.bolg;

/**
 * Protobuf type {@code bolg.Player}
 */
public  final class Player extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:bolg.Player)
    PlayerOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Player.newBuilder() to construct.
  private Player(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Player() {
    id_ = 0L;
    name_ = "";
    hp_ = 0L;
    ready_ = false;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Player(
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
          case 8: {

            id_ = input.readInt64();
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            name_ = s;
            break;
          }
          case 24: {

            hp_ = input.readInt64();
            break;
          }
          case 32: {

            ready_ = input.readBool();
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
    return org.bolg_developers.bolg.BolgProto.internal_static_bolg_Player_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.bolg_developers.bolg.BolgProto.internal_static_bolg_Player_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.bolg_developers.bolg.Player.class, org.bolg_developers.bolg.Player.Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  private long id_;
  /**
   * <pre>
   * output only
   * idは赤外線信号にも利用されます。
   * </pre>
   *
   * <code>int64 id = 1;</code>
   */
  public long getId() {
    return id_;
  }

  public static final int NAME_FIELD_NUMBER = 2;
  private volatile java.lang.Object name_;
  /**
   * <pre>
   * nameはUserのnameと同じです。
   * </pre>
   *
   * <code>string name = 2;</code>
   */
  public java.lang.String getName() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      name_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * nameはUserのnameと同じです。
   * </pre>
   *
   * <code>string name = 2;</code>
   */
  public com.google.protobuf.ByteString
      getNameBytes() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      name_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int HP_FIELD_NUMBER = 3;
  private long hp_;
  /**
   * <pre>
   * output only
   * </pre>
   *
   * <code>int64 hp = 3;</code>
   */
  public long getHp() {
    return hp_;
  }

  public static final int READY_FIELD_NUMBER = 4;
  private boolean ready_;
  /**
   * <pre>
   * output only
   * readyはゲームを行うための準備ができたかどうかを表します。
   * ルーム内のすべてのプレイヤーのreadyがtrueになったとき、
   * ゲームを開始することができます。
   * </pre>
   *
   * <code>bool ready = 4;</code>
   */
  public boolean getReady() {
    return ready_;
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
    if (id_ != 0L) {
      output.writeInt64(1, id_);
    }
    if (!getNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, name_);
    }
    if (hp_ != 0L) {
      output.writeInt64(3, hp_);
    }
    if (ready_ != false) {
      output.writeBool(4, ready_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (id_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, id_);
    }
    if (!getNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, name_);
    }
    if (hp_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(3, hp_);
    }
    if (ready_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(4, ready_);
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
    if (!(obj instanceof org.bolg_developers.bolg.Player)) {
      return super.equals(obj);
    }
    org.bolg_developers.bolg.Player other = (org.bolg_developers.bolg.Player) obj;

    boolean result = true;
    result = result && (getId()
        == other.getId());
    result = result && getName()
        .equals(other.getName());
    result = result && (getHp()
        == other.getHp());
    result = result && (getReady()
        == other.getReady());
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
    hash = (37 * hash) + ID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getId());
    hash = (37 * hash) + NAME_FIELD_NUMBER;
    hash = (53 * hash) + getName().hashCode();
    hash = (37 * hash) + HP_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getHp());
    hash = (37 * hash) + READY_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getReady());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.bolg_developers.bolg.Player parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bolg_developers.bolg.Player parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bolg_developers.bolg.Player parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bolg_developers.bolg.Player parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bolg_developers.bolg.Player parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bolg_developers.bolg.Player parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bolg_developers.bolg.Player parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.bolg_developers.bolg.Player parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.bolg_developers.bolg.Player parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.bolg_developers.bolg.Player parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.bolg_developers.bolg.Player parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.bolg_developers.bolg.Player parseFrom(
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
  public static Builder newBuilder(org.bolg_developers.bolg.Player prototype) {
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
   * Protobuf type {@code bolg.Player}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:bolg.Player)
      org.bolg_developers.bolg.PlayerOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.bolg_developers.bolg.BolgProto.internal_static_bolg_Player_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.bolg_developers.bolg.BolgProto.internal_static_bolg_Player_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.bolg_developers.bolg.Player.class, org.bolg_developers.bolg.Player.Builder.class);
    }

    // Construct using org.bolg_developers.bolg.Player.newBuilder()
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
      id_ = 0L;

      name_ = "";

      hp_ = 0L;

      ready_ = false;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.bolg_developers.bolg.BolgProto.internal_static_bolg_Player_descriptor;
    }

    public org.bolg_developers.bolg.Player getDefaultInstanceForType() {
      return org.bolg_developers.bolg.Player.getDefaultInstance();
    }

    public org.bolg_developers.bolg.Player build() {
      org.bolg_developers.bolg.Player result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public org.bolg_developers.bolg.Player buildPartial() {
      org.bolg_developers.bolg.Player result = new org.bolg_developers.bolg.Player(this);
      result.id_ = id_;
      result.name_ = name_;
      result.hp_ = hp_;
      result.ready_ = ready_;
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
      if (other instanceof org.bolg_developers.bolg.Player) {
        return mergeFrom((org.bolg_developers.bolg.Player)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.bolg_developers.bolg.Player other) {
      if (other == org.bolg_developers.bolg.Player.getDefaultInstance()) return this;
      if (other.getId() != 0L) {
        setId(other.getId());
      }
      if (!other.getName().isEmpty()) {
        name_ = other.name_;
        onChanged();
      }
      if (other.getHp() != 0L) {
        setHp(other.getHp());
      }
      if (other.getReady() != false) {
        setReady(other.getReady());
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
      org.bolg_developers.bolg.Player parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.bolg_developers.bolg.Player) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private long id_ ;
    /**
     * <pre>
     * output only
     * idは赤外線信号にも利用されます。
     * </pre>
     *
     * <code>int64 id = 1;</code>
     */
    public long getId() {
      return id_;
    }
    /**
     * <pre>
     * output only
     * idは赤外線信号にも利用されます。
     * </pre>
     *
     * <code>int64 id = 1;</code>
     */
    public Builder setId(long value) {
      
      id_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * output only
     * idは赤外線信号にも利用されます。
     * </pre>
     *
     * <code>int64 id = 1;</code>
     */
    public Builder clearId() {
      
      id_ = 0L;
      onChanged();
      return this;
    }

    private java.lang.Object name_ = "";
    /**
     * <pre>
     * nameはUserのnameと同じです。
     * </pre>
     *
     * <code>string name = 2;</code>
     */
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        name_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * nameはUserのnameと同じです。
     * </pre>
     *
     * <code>string name = 2;</code>
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * nameはUserのnameと同じです。
     * </pre>
     *
     * <code>string name = 2;</code>
     */
    public Builder setName(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      name_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * nameはUserのnameと同じです。
     * </pre>
     *
     * <code>string name = 2;</code>
     */
    public Builder clearName() {
      
      name_ = getDefaultInstance().getName();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * nameはUserのnameと同じです。
     * </pre>
     *
     * <code>string name = 2;</code>
     */
    public Builder setNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      name_ = value;
      onChanged();
      return this;
    }

    private long hp_ ;
    /**
     * <pre>
     * output only
     * </pre>
     *
     * <code>int64 hp = 3;</code>
     */
    public long getHp() {
      return hp_;
    }
    /**
     * <pre>
     * output only
     * </pre>
     *
     * <code>int64 hp = 3;</code>
     */
    public Builder setHp(long value) {
      
      hp_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * output only
     * </pre>
     *
     * <code>int64 hp = 3;</code>
     */
    public Builder clearHp() {
      
      hp_ = 0L;
      onChanged();
      return this;
    }

    private boolean ready_ ;
    /**
     * <pre>
     * output only
     * readyはゲームを行うための準備ができたかどうかを表します。
     * ルーム内のすべてのプレイヤーのreadyがtrueになったとき、
     * ゲームを開始することができます。
     * </pre>
     *
     * <code>bool ready = 4;</code>
     */
    public boolean getReady() {
      return ready_;
    }
    /**
     * <pre>
     * output only
     * readyはゲームを行うための準備ができたかどうかを表します。
     * ルーム内のすべてのプレイヤーのreadyがtrueになったとき、
     * ゲームを開始することができます。
     * </pre>
     *
     * <code>bool ready = 4;</code>
     */
    public Builder setReady(boolean value) {
      
      ready_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * output only
     * readyはゲームを行うための準備ができたかどうかを表します。
     * ルーム内のすべてのプレイヤーのreadyがtrueになったとき、
     * ゲームを開始することができます。
     * </pre>
     *
     * <code>bool ready = 4;</code>
     */
    public Builder clearReady() {
      
      ready_ = false;
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


    // @@protoc_insertion_point(builder_scope:bolg.Player)
  }

  // @@protoc_insertion_point(class_scope:bolg.Player)
  private static final org.bolg_developers.bolg.Player DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.bolg_developers.bolg.Player();
  }

  public static org.bolg_developers.bolg.Player getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Player>
      PARSER = new com.google.protobuf.AbstractParser<Player>() {
    public Player parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Player(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Player> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Player> getParserForType() {
    return PARSER;
  }

  public org.bolg_developers.bolg.Player getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

