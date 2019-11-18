package org.bolg_developers.bolg;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 *==============================================================================
 * Room Service
 *==============================================================================
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.22.0-SNAPSHOT)",
    comments = "Source: server/pb/bolg.proto")
public final class RoomServiceGrpc {

  private RoomServiceGrpc() {}

  public static final String SERVICE_NAME = "bolg.RoomService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.bolg_developers.bolg.RoomMessage,
      org.bolg_developers.bolg.RoomMessage> getCreateAndJoinRoomMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateAndJoinRoom",
      requestType = org.bolg_developers.bolg.RoomMessage.class,
      responseType = org.bolg_developers.bolg.RoomMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<org.bolg_developers.bolg.RoomMessage,
      org.bolg_developers.bolg.RoomMessage> getCreateAndJoinRoomMethod() {
    io.grpc.MethodDescriptor<org.bolg_developers.bolg.RoomMessage, org.bolg_developers.bolg.RoomMessage> getCreateAndJoinRoomMethod;
    if ((getCreateAndJoinRoomMethod = RoomServiceGrpc.getCreateAndJoinRoomMethod) == null) {
      synchronized (RoomServiceGrpc.class) {
        if ((getCreateAndJoinRoomMethod = RoomServiceGrpc.getCreateAndJoinRoomMethod) == null) {
          RoomServiceGrpc.getCreateAndJoinRoomMethod = getCreateAndJoinRoomMethod = 
              io.grpc.MethodDescriptor.<org.bolg_developers.bolg.RoomMessage, org.bolg_developers.bolg.RoomMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "bolg.RoomService", "CreateAndJoinRoom"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.RoomMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.RoomMessage.getDefaultInstance()))
                  .build();
          }
        }
     }
     return getCreateAndJoinRoomMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RoomServiceStub newStub(io.grpc.Channel channel) {
    return new RoomServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RoomServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RoomServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RoomServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RoomServiceFutureStub(channel);
  }

  /**
   * <pre>
   *==============================================================================
   * Room Service
   *==============================================================================
   * </pre>
   */
  public static abstract class RoomServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RoomMessage> createAndJoinRoom(
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RoomMessage> responseObserver) {
      return asyncUnimplementedStreamingCall(getCreateAndJoinRoomMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCreateAndJoinRoomMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                org.bolg_developers.bolg.RoomMessage,
                org.bolg_developers.bolg.RoomMessage>(
                  this, METHODID_CREATE_AND_JOIN_ROOM)))
          .build();
    }
  }

  /**
   * <pre>
   *==============================================================================
   * Room Service
   *==============================================================================
   * </pre>
   */
  public static final class RoomServiceStub extends io.grpc.stub.AbstractStub<RoomServiceStub> {
    private RoomServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RoomServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RoomServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RoomServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RoomMessage> createAndJoinRoom(
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RoomMessage> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getCreateAndJoinRoomMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * <pre>
   *==============================================================================
   * Room Service
   *==============================================================================
   * </pre>
   */
  public static final class RoomServiceBlockingStub extends io.grpc.stub.AbstractStub<RoomServiceBlockingStub> {
    private RoomServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RoomServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RoomServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RoomServiceBlockingStub(channel, callOptions);
    }
  }

  /**
   * <pre>
   *==============================================================================
   * Room Service
   *==============================================================================
   * </pre>
   */
  public static final class RoomServiceFutureStub extends io.grpc.stub.AbstractStub<RoomServiceFutureStub> {
    private RoomServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RoomServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RoomServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RoomServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_CREATE_AND_JOIN_ROOM = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RoomServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RoomServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_AND_JOIN_ROOM:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.createAndJoinRoom(
              (io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RoomMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RoomServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getCreateAndJoinRoomMethod())
              .build();
        }
      }
    }
    return result;
  }
}
