// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: server/pb/bolg.proto

package org.bolg_developers.bolg;

/**
 * Protobuf type {@code bolg.RecoverStaminaResponse}
 */
public  final class RecoverStaminaResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:bolg.RecoverStaminaResponse)
    RecoverStaminaResponseOrBuilder {
private static final long serialVersionUID = 0L;
  // Use RecoverStaminaResponse.newBuilder() to construct.
  private RecoverStaminaResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RecoverStaminaResponse() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private RecoverStaminaResponse(
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
            org.bolg_developers.bolg.Stamina.Builder subBuilder = null;
            if (stamina_ != null) {
              subBuilder = stamina_.toBuilder();
            }
            stamina_ = input.readMessage(org.bolg_developers.bolg.Stamina.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(stamina_);
              stamina_ = subBuilder.buildPartial();
            }

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
    return org.bolg_developers.bolg.BolgProto.internal_static_bolg_RecoverStaminaResponse_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.bolg_developers.bolg.BolgProto.internal_static_bolg_RecoverStaminaResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.bolg_developers.bolg.RecoverStaminaResponse.class, org.bolg_developers.bolg.RecoverStaminaResponse.Builder.class);
  }

  public static final int STAMINA_FIELD_NUMBER = 1;
  private org.bolg_developers.bolg.Stamina stamina_;
  /**
   * <code>.bolg.Stamina stamina = 1;</code>
   */
  public boolean hasStamina() {
    return stamina_ != null;
  }
  /**
   * <code>.bolg.Stamina stamina = 1;</code>
   */
  public org.bolg_developers.bolg.Stamina getStamina() {
    return stamina_ == null ? org.bolg_developers.bolg.Stamina.getDefaultInstance() : stamina_;
  }
  /**
   * <code>.bolg.Stamina stamina = 1;</code>
   */
  public org.bolg_developers.bolg.StaminaOrBuilder getStaminaOrBuilder() {
    return getStamina();
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
    if (stamina_ != null) {
      output.writeMessage(1, getStamina());
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (stamina_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getStamina());
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
    if (!(obj instanceof org.bolg_developers.bolg.RecoverStaminaResponse)) {
      return super.equals(obj);
    }
    org.bolg_developers.bolg.RecoverStaminaResponse other = (org.bolg_developers.bolg.RecoverStaminaResponse) obj;

    boolean result = true;
    result = result && (hasStamina() == other.hasStamina());
    if (hasStamina()) {
      result = result && getStamina()
          .equals(other.getStamina());
    }
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
    if (hasStamina()) {
      hash = (37 * hash) + STAMINA_FIELD_NUMBER;
      hash = (53 * hash) + getStamina().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.bolg_developers.bolg.RecoverStaminaResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bolg_developers.bolg.RecoverStaminaResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bolg_developers.bolg.RecoverStaminaResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bolg_developers.bolg.RecoverStaminaResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bolg_developers.bolg.RecoverStaminaResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.bolg_developers.bolg.RecoverStaminaResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.bolg_developers.bolg.RecoverStaminaResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.bolg_developers.bolg.RecoverStaminaResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.bolg_developers.bolg.RecoverStaminaResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.bolg_developers.bolg.RecoverStaminaResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.bolg_developers.bolg.RecoverStaminaResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.bolg_developers.bolg.RecoverStaminaResponse parseFrom(
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
  public static Builder newBuilder(org.bolg_developers.bolg.RecoverStaminaResponse prototype) {
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
   * Protobuf type {@code bolg.RecoverStaminaResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:bolg.RecoverStaminaResponse)
      org.bolg_developers.bolg.RecoverStaminaResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.bolg_developers.bolg.BolgProto.internal_static_bolg_RecoverStaminaResponse_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.bolg_developers.bolg.BolgProto.internal_static_bolg_RecoverStaminaResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.bolg_developers.bolg.RecoverStaminaResponse.class, org.bolg_developers.bolg.RecoverStaminaResponse.Builder.class);
    }

    // Construct using org.bolg_developers.bolg.RecoverStaminaResponse.newBuilder()
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
      if (staminaBuilder_ == null) {
        stamina_ = null;
      } else {
        stamina_ = null;
        staminaBuilder_ = null;
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.bolg_developers.bolg.BolgProto.internal_static_bolg_RecoverStaminaResponse_descriptor;
    }

    public org.bolg_developers.bolg.RecoverStaminaResponse getDefaultInstanceForType() {
      return org.bolg_developers.bolg.RecoverStaminaResponse.getDefaultInstance();
    }

    public org.bolg_developers.bolg.RecoverStaminaResponse build() {
      org.bolg_developers.bolg.RecoverStaminaResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public org.bolg_developers.bolg.RecoverStaminaResponse buildPartial() {
      org.bolg_developers.bolg.RecoverStaminaResponse result = new org.bolg_developers.bolg.RecoverStaminaResponse(this);
      if (staminaBuilder_ == null) {
        result.stamina_ = stamina_;
      } else {
        result.stamina_ = staminaBuilder_.build();
      }
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
      if (other instanceof org.bolg_developers.bolg.RecoverStaminaResponse) {
        return mergeFrom((org.bolg_developers.bolg.RecoverStaminaResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.bolg_developers.bolg.RecoverStaminaResponse other) {
      if (other == org.bolg_developers.bolg.RecoverStaminaResponse.getDefaultInstance()) return this;
      if (other.hasStamina()) {
        mergeStamina(other.getStamina());
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
      org.bolg_developers.bolg.RecoverStaminaResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.bolg_developers.bolg.RecoverStaminaResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private org.bolg_developers.bolg.Stamina stamina_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.bolg_developers.bolg.Stamina, org.bolg_developers.bolg.Stamina.Builder, org.bolg_developers.bolg.StaminaOrBuilder> staminaBuilder_;
    /**
     * <code>.bolg.Stamina stamina = 1;</code>
     */
    public boolean hasStamina() {
      return staminaBuilder_ != null || stamina_ != null;
    }
    /**
     * <code>.bolg.Stamina stamina = 1;</code>
     */
    public org.bolg_developers.bolg.Stamina getStamina() {
      if (staminaBuilder_ == null) {
        return stamina_ == null ? org.bolg_developers.bolg.Stamina.getDefaultInstance() : stamina_;
      } else {
        return staminaBuilder_.getMessage();
      }
    }
    /**
     * <code>.bolg.Stamina stamina = 1;</code>
     */
    public Builder setStamina(org.bolg_developers.bolg.Stamina value) {
      if (staminaBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        stamina_ = value;
        onChanged();
      } else {
        staminaBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.bolg.Stamina stamina = 1;</code>
     */
    public Builder setStamina(
        org.bolg_developers.bolg.Stamina.Builder builderForValue) {
      if (staminaBuilder_ == null) {
        stamina_ = builderForValue.build();
        onChanged();
      } else {
        staminaBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.bolg.Stamina stamina = 1;</code>
     */
    public Builder mergeStamina(org.bolg_developers.bolg.Stamina value) {
      if (staminaBuilder_ == null) {
        if (stamina_ != null) {
          stamina_ =
            org.bolg_developers.bolg.Stamina.newBuilder(stamina_).mergeFrom(value).buildPartial();
        } else {
          stamina_ = value;
        }
        onChanged();
      } else {
        staminaBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.bolg.Stamina stamina = 1;</code>
     */
    public Builder clearStamina() {
      if (staminaBuilder_ == null) {
        stamina_ = null;
        onChanged();
      } else {
        stamina_ = null;
        staminaBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.bolg.Stamina stamina = 1;</code>
     */
    public org.bolg_developers.bolg.Stamina.Builder getStaminaBuilder() {
      
      onChanged();
      return getStaminaFieldBuilder().getBuilder();
    }
    /**
     * <code>.bolg.Stamina stamina = 1;</code>
     */
    public org.bolg_developers.bolg.StaminaOrBuilder getStaminaOrBuilder() {
      if (staminaBuilder_ != null) {
        return staminaBuilder_.getMessageOrBuilder();
      } else {
        return stamina_ == null ?
            org.bolg_developers.bolg.Stamina.getDefaultInstance() : stamina_;
      }
    }
    /**
     * <code>.bolg.Stamina stamina = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.bolg_developers.bolg.Stamina, org.bolg_developers.bolg.Stamina.Builder, org.bolg_developers.bolg.StaminaOrBuilder> 
        getStaminaFieldBuilder() {
      if (staminaBuilder_ == null) {
        staminaBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.bolg_developers.bolg.Stamina, org.bolg_developers.bolg.Stamina.Builder, org.bolg_developers.bolg.StaminaOrBuilder>(
                getStamina(),
                getParentForChildren(),
                isClean());
        stamina_ = null;
      }
      return staminaBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:bolg.RecoverStaminaResponse)
  }

  // @@protoc_insertion_point(class_scope:bolg.RecoverStaminaResponse)
  private static final org.bolg_developers.bolg.RecoverStaminaResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.bolg_developers.bolg.RecoverStaminaResponse();
  }

  public static org.bolg_developers.bolg.RecoverStaminaResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RecoverStaminaResponse>
      PARSER = new com.google.protobuf.AbstractParser<RecoverStaminaResponse>() {
    public RecoverStaminaResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new RecoverStaminaResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<RecoverStaminaResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RecoverStaminaResponse> getParserForType() {
    return PARSER;
  }

  public org.bolg_developers.bolg.RecoverStaminaResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

