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
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.22.0-SNAPSHOT)",
    comments = "Source: server/pb/bolg.proto")
public final class BolgServiceGrpc {

  private BolgServiceGrpc() {}

  public static final String SERVICE_NAME = "bolg.BolgService";

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
    if ((getCreateAndJoinRoomMethod = BolgServiceGrpc.getCreateAndJoinRoomMethod) == null) {
      synchronized (BolgServiceGrpc.class) {
        if ((getCreateAndJoinRoomMethod = BolgServiceGrpc.getCreateAndJoinRoomMethod) == null) {
          BolgServiceGrpc.getCreateAndJoinRoomMethod = getCreateAndJoinRoomMethod = 
              io.grpc.MethodDescriptor.<org.bolg_developers.bolg.RoomMessage, org.bolg_developers.bolg.RoomMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "bolg.BolgService", "CreateAndJoinRoom"))
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
  public static BolgServiceStub newStub(io.grpc.Channel channel) {
    return new BolgServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BolgServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new BolgServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BolgServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new BolgServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class BolgServiceImplBase implements io.grpc.BindableService {

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
   */
  public static final class BolgServiceStub extends io.grpc.stub.AbstractStub<BolgServiceStub> {
    private BolgServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BolgServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BolgServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BolgServiceStub(channel, callOptions);
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
   */
  public static final class BolgServiceBlockingStub extends io.grpc.stub.AbstractStub<BolgServiceBlockingStub> {
    private BolgServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BolgServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BolgServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BolgServiceBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class BolgServiceFutureStub extends io.grpc.stub.AbstractStub<BolgServiceFutureStub> {
    private BolgServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BolgServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BolgServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BolgServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_CREATE_AND_JOIN_ROOM = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BolgServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BolgServiceImplBase serviceImpl, int methodId) {
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
      synchronized (BolgServiceGrpc.class) {
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
