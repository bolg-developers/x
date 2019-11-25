package com.example.bolg

import android.util.Log
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import org.bolg_developers.bolg.BolgServiceGrpc
import org.bolg_developers.bolg.CreateAndJoinRoomRequest
import org.bolg_developers.bolg.RoomMessage

class GrpcTask {

    private var channel: ManagedChannel
    private var blockingStub: BolgServiceGrpc.BolgServiceBlockingStub?
    private var asyncStub: BolgServiceGrpc.BolgServiceStub
    private lateinit var observer: StreamObserver<RoomMessage>
    private lateinit var message: RoomMessage

    init {
        Log.d("BolgGrpcTask","init")
        channel = ManagedChannelBuilder.forAddress("host", 0)
            .usePlaintext()
            .build()

        asyncStub = BolgServiceGrpc.newStub(channel)
        blockingStub = BolgServiceGrpc.newBlockingStub(channel)
    }


    fun createAndJoinRoomTask(pName: String) : RoomMessage {
        Log.d("BolgGrpcTask", "createRoom")
        val reqMessage = CreateAndJoinRoomRequest.newBuilder().setPlayerName(pName).build()
        message = RoomMessage.newBuilder().setCreateAndJoinRoomReq(reqMessage).build()
        response()
        observer.onNext(message)
        return message
    }

    fun response(){
        observer = asyncStub.connect(object : StreamObserver<RoomMessage>{
            override fun onNext(value: RoomMessage) {
                Log.d("StreamObserver", "onNext: res -> ${value.toString()}")
                message = value
            }
            override fun onError(t: Throwable) {
                Log.d("StreamObserver", "onError: ${t.message}")

                when(t.message) {
                    "UNAUTHENTICATED" -> Log.d("Error", "ErrInvalidToken")
                    "NOTFOUND"->  Log.d("Error", "ErrNotFound")
                    "ALREADYEXISTS" -> Log.d("Error", "ErrAlreadyExists")
                    "FAILEDPRECONDITION" -> Log.d("Error", "ErrHPisZero")
                    else -> Log.d("Error", "Internal")
                }
            }
            override fun onCompleted() {
                Log.d("StreamObserver", "onCompleted")
            }
        })
    }
}