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
      org.bolg_developers.bolg.RoomMessage> getConnectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Connect",
      requestType = org.bolg_developers.bolg.RoomMessage.class,
      responseType = org.bolg_developers.bolg.RoomMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<org.bolg_developers.bolg.RoomMessage,
      org.bolg_developers.bolg.RoomMessage> getConnectMethod() {
    io.grpc.MethodDescriptor<org.bolg_developers.bolg.RoomMessage, org.bolg_developers.bolg.RoomMessage> getConnectMethod;
    if ((getConnectMethod = BolgServiceGrpc.getConnectMethod) == null) {
      synchronized (BolgServiceGrpc.class) {
        if ((getConnectMethod = BolgServiceGrpc.getConnectMethod) == null) {
          BolgServiceGrpc.getConnectMethod = getConnectMethod = 
              io.grpc.MethodDescriptor.<org.bolg_developers.bolg.RoomMessage, org.bolg_developers.bolg.RoomMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "bolg.BolgService", "Connect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.RoomMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.RoomMessage.getDefaultInstance()))
                  .build();
          }
        }
     }
     return getConnectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.bolg_developers.bolg.CheckHealthRequest,
      org.bolg_developers.bolg.CheckHealthResponse> getCheckHealthMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckHealth",
      requestType = org.bolg_developers.bolg.CheckHealthRequest.class,
      responseType = org.bolg_developers.bolg.CheckHealthResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.bolg_developers.bolg.CheckHealthRequest,
      org.bolg_developers.bolg.CheckHealthResponse> getCheckHealthMethod() {
    io.grpc.MethodDescriptor<org.bolg_developers.bolg.CheckHealthRequest, org.bolg_developers.bolg.CheckHealthResponse> getCheckHealthMethod;
    if ((getCheckHealthMethod = BolgServiceGrpc.getCheckHealthMethod) == null) {
      synchronized (BolgServiceGrpc.class) {
        if ((getCheckHealthMethod = BolgServiceGrpc.getCheckHealthMethod) == null) {
          BolgServiceGrpc.getCheckHealthMethod = getCheckHealthMethod = 
              io.grpc.MethodDescriptor.<org.bolg_developers.bolg.CheckHealthRequest, org.bolg_developers.bolg.CheckHealthResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "bolg.BolgService", "CheckHealth"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.CheckHealthRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.CheckHealthResponse.getDefaultInstance()))
                  .build();
          }
        }
     }
     return getCheckHealthMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.bolg_developers.bolg.GetStaminaRequest,
      org.bolg_developers.bolg.Stamina> getGetStaminaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetStamina",
      requestType = org.bolg_developers.bolg.GetStaminaRequest.class,
      responseType = org.bolg_developers.bolg.Stamina.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.bolg_developers.bolg.GetStaminaRequest,
      org.bolg_developers.bolg.Stamina> getGetStaminaMethod() {
    io.grpc.MethodDescriptor<org.bolg_developers.bolg.GetStaminaRequest, org.bolg_developers.bolg.Stamina> getGetStaminaMethod;
    if ((getGetStaminaMethod = BolgServiceGrpc.getGetStaminaMethod) == null) {
      synchronized (BolgServiceGrpc.class) {
        if ((getGetStaminaMethod = BolgServiceGrpc.getGetStaminaMethod) == null) {
          BolgServiceGrpc.getGetStaminaMethod = getGetStaminaMethod = 
              io.grpc.MethodDescriptor.<org.bolg_developers.bolg.GetStaminaRequest, org.bolg_developers.bolg.Stamina>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "bolg.BolgService", "GetStamina"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.GetStaminaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.Stamina.getDefaultInstance()))
                  .build();
          }
        }
     }
     return getGetStaminaMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.bolg_developers.bolg.UseStaminaRequest,
      org.bolg_developers.bolg.UseStaminaResponse> getUseStaminaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UseStamina",
      requestType = org.bolg_developers.bolg.UseStaminaRequest.class,
      responseType = org.bolg_developers.bolg.UseStaminaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.bolg_developers.bolg.UseStaminaRequest,
      org.bolg_developers.bolg.UseStaminaResponse> getUseStaminaMethod() {
    io.grpc.MethodDescriptor<org.bolg_developers.bolg.UseStaminaRequest, org.bolg_developers.bolg.UseStaminaResponse> getUseStaminaMethod;
    if ((getUseStaminaMethod = BolgServiceGrpc.getUseStaminaMethod) == null) {
      synchronized (BolgServiceGrpc.class) {
        if ((getUseStaminaMethod = BolgServiceGrpc.getUseStaminaMethod) == null) {
          BolgServiceGrpc.getUseStaminaMethod = getUseStaminaMethod = 
              io.grpc.MethodDescriptor.<org.bolg_developers.bolg.UseStaminaRequest, org.bolg_developers.bolg.UseStaminaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "bolg.BolgService", "UseStamina"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.UseStaminaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.UseStaminaResponse.getDefaultInstance()))
                  .build();
          }
        }
     }
     return getUseStaminaMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.bolg_developers.bolg.RecoverStaminaRequest,
      org.bolg_developers.bolg.RecoverStaminaResponse> getRecoverStaminaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RecoverStamina",
      requestType = org.bolg_developers.bolg.RecoverStaminaRequest.class,
      responseType = org.bolg_developers.bolg.RecoverStaminaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.bolg_developers.bolg.RecoverStaminaRequest,
      org.bolg_developers.bolg.RecoverStaminaResponse> getRecoverStaminaMethod() {
    io.grpc.MethodDescriptor<org.bolg_developers.bolg.RecoverStaminaRequest, org.bolg_developers.bolg.RecoverStaminaResponse> getRecoverStaminaMethod;
    if ((getRecoverStaminaMethod = BolgServiceGrpc.getRecoverStaminaMethod) == null) {
      synchronized (BolgServiceGrpc.class) {
        if ((getRecoverStaminaMethod = BolgServiceGrpc.getRecoverStaminaMethod) == null) {
          BolgServiceGrpc.getRecoverStaminaMethod = getRecoverStaminaMethod = 
              io.grpc.MethodDescriptor.<org.bolg_developers.bolg.RecoverStaminaRequest, org.bolg_developers.bolg.RecoverStaminaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "bolg.BolgService", "RecoverStamina"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.RecoverStaminaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  org.bolg_developers.bolg.RecoverStaminaResponse.getDefaultInstance()))
                  .build();
          }
        }
     }
     return getRecoverStaminaMethod;
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
     * <pre>
     * ConnectはRoom Messagingを行うためのRPCです。
     * </pre>
     */
    public io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RoomMessage> connect(
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RoomMessage> responseObserver) {
      return asyncUnimplementedStreamingCall(getConnectMethod(), responseObserver);
    }

    /**
     * <pre>
     * CheckHealth はサーバーが生存しているかチェックするためのRPCです。
     * </pre>
     */
    public void checkHealth(org.bolg_developers.bolg.CheckHealthRequest request,
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.CheckHealthResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCheckHealthMethod(), responseObserver);
    }

    /**
     */
    public void getStamina(org.bolg_developers.bolg.GetStaminaRequest request,
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.Stamina> responseObserver) {
      asyncUnimplementedUnaryCall(getGetStaminaMethod(), responseObserver);
    }

    /**
     * <pre>
     *  UseStamina はスタミナを消費するためのRPCです。
     * </pre>
     */
    public void useStamina(org.bolg_developers.bolg.UseStaminaRequest request,
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.UseStaminaResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getUseStaminaMethod(), responseObserver);
    }

    /**
     * <pre>
     * RecoverStamina はスタミナを強制回復するためのRPCです。
     * デバッグ用のRPCあることに注意してください！！！！！！！！！！！！！
     * </pre>
     */
    public void recoverStamina(org.bolg_developers.bolg.RecoverStaminaRequest request,
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RecoverStaminaResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRecoverStaminaMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getConnectMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                org.bolg_developers.bolg.RoomMessage,
                org.bolg_developers.bolg.RoomMessage>(
                  this, METHODID_CONNECT)))
          .addMethod(
            getCheckHealthMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.bolg_developers.bolg.CheckHealthRequest,
                org.bolg_developers.bolg.CheckHealthResponse>(
                  this, METHODID_CHECK_HEALTH)))
          .addMethod(
            getGetStaminaMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.bolg_developers.bolg.GetStaminaRequest,
                org.bolg_developers.bolg.Stamina>(
                  this, METHODID_GET_STAMINA)))
          .addMethod(
            getUseStaminaMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.bolg_developers.bolg.UseStaminaRequest,
                org.bolg_developers.bolg.UseStaminaResponse>(
                  this, METHODID_USE_STAMINA)))
          .addMethod(
            getRecoverStaminaMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.bolg_developers.bolg.RecoverStaminaRequest,
                org.bolg_developers.bolg.RecoverStaminaResponse>(
                  this, METHODID_RECOVER_STAMINA)))
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
     * <pre>
     * ConnectはRoom Messagingを行うためのRPCです。
     * </pre>
     */
    public io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RoomMessage> connect(
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RoomMessage> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getConnectMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * CheckHealth はサーバーが生存しているかチェックするためのRPCです。
     * </pre>
     */
    public void checkHealth(org.bolg_developers.bolg.CheckHealthRequest request,
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.CheckHealthResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCheckHealthMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getStamina(org.bolg_developers.bolg.GetStaminaRequest request,
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.Stamina> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetStaminaMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *  UseStamina はスタミナを消費するためのRPCです。
     * </pre>
     */
    public void useStamina(org.bolg_developers.bolg.UseStaminaRequest request,
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.UseStaminaResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUseStaminaMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * RecoverStamina はスタミナを強制回復するためのRPCです。
     * デバッグ用のRPCあることに注意してください！！！！！！！！！！！！！
     * </pre>
     */
    public void recoverStamina(org.bolg_developers.bolg.RecoverStaminaRequest request,
        io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RecoverStaminaResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRecoverStaminaMethod(), getCallOptions()), request, responseObserver);
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

    /**
     * <pre>
     * CheckHealth はサーバーが生存しているかチェックするためのRPCです。
     * </pre>
     */
    public org.bolg_developers.bolg.CheckHealthResponse checkHealth(org.bolg_developers.bolg.CheckHealthRequest request) {
      return blockingUnaryCall(
          getChannel(), getCheckHealthMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.bolg_developers.bolg.Stamina getStamina(org.bolg_developers.bolg.GetStaminaRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetStaminaMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *  UseStamina はスタミナを消費するためのRPCです。
     * </pre>
     */
    public org.bolg_developers.bolg.UseStaminaResponse useStamina(org.bolg_developers.bolg.UseStaminaRequest request) {
      return blockingUnaryCall(
          getChannel(), getUseStaminaMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * RecoverStamina はスタミナを強制回復するためのRPCです。
     * デバッグ用のRPCあることに注意してください！！！！！！！！！！！！！
     * </pre>
     */
    public org.bolg_developers.bolg.RecoverStaminaResponse recoverStamina(org.bolg_developers.bolg.RecoverStaminaRequest request) {
      return blockingUnaryCall(
          getChannel(), getRecoverStaminaMethod(), getCallOptions(), request);
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

    /**
     * <pre>
     * CheckHealth はサーバーが生存しているかチェックするためのRPCです。
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.bolg_developers.bolg.CheckHealthResponse> checkHealth(
        org.bolg_developers.bolg.CheckHealthRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckHealthMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.bolg_developers.bolg.Stamina> getStamina(
        org.bolg_developers.bolg.GetStaminaRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetStaminaMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *  UseStamina はスタミナを消費するためのRPCです。
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.bolg_developers.bolg.UseStaminaResponse> useStamina(
        org.bolg_developers.bolg.UseStaminaRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUseStaminaMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * RecoverStamina はスタミナを強制回復するためのRPCです。
     * デバッグ用のRPCあることに注意してください！！！！！！！！！！！！！
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.bolg_developers.bolg.RecoverStaminaResponse> recoverStamina(
        org.bolg_developers.bolg.RecoverStaminaRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRecoverStaminaMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CHECK_HEALTH = 0;
  private static final int METHODID_GET_STAMINA = 1;
  private static final int METHODID_USE_STAMINA = 2;
  private static final int METHODID_RECOVER_STAMINA = 3;
  private static final int METHODID_CONNECT = 4;

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
        case METHODID_CHECK_HEALTH:
          serviceImpl.checkHealth((org.bolg_developers.bolg.CheckHealthRequest) request,
              (io.grpc.stub.StreamObserver<org.bolg_developers.bolg.CheckHealthResponse>) responseObserver);
          break;
        case METHODID_GET_STAMINA:
          serviceImpl.getStamina((org.bolg_developers.bolg.GetStaminaRequest) request,
              (io.grpc.stub.StreamObserver<org.bolg_developers.bolg.Stamina>) responseObserver);
          break;
        case METHODID_USE_STAMINA:
          serviceImpl.useStamina((org.bolg_developers.bolg.UseStaminaRequest) request,
              (io.grpc.stub.StreamObserver<org.bolg_developers.bolg.UseStaminaResponse>) responseObserver);
          break;
        case METHODID_RECOVER_STAMINA:
          serviceImpl.recoverStamina((org.bolg_developers.bolg.RecoverStaminaRequest) request,
              (io.grpc.stub.StreamObserver<org.bolg_developers.bolg.RecoverStaminaResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CONNECT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.connect(
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
              .addMethod(getConnectMethod())
              .addMethod(getCheckHealthMethod())
              .addMethod(getGetStaminaMethod())
              .addMethod(getUseStaminaMethod())
              .addMethod(getRecoverStaminaMethod())
              .build();
        }
      }
    }
    return result;
  }
}
