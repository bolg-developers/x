package com.example.bolg

import android.util.Log
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import org.bolg_developers.bolg.BolgServiceGrpc
import org.bolg_developers.bolg.CreateAndJoinRoomRequest
import org.bolg_developers.bolg.RoomMessage

/** ----------------------------------------------------------------------
 * GrpcTask
 * Serverとの通信を担う
 * ---------------------------------------------------------------------- */
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


    /** **********************************************************************
     * createAndJoinRoomTask
     * @param pName playerの名前
     * 部屋を作成し、入室する
     * ********************************************************************** */
    fun createAndJoinRoomTask(pName: String) : RoomMessage {
        Log.d("BolgGrpcTask", "createRoom")
        val reqMessage = CreateAndJoinRoomRequest.newBuilder().setPlayerName(pName).build()
        message = RoomMessage.newBuilder().setCreateAndJoinRoomReq(reqMessage).build()
        response()
        observer.onNext(message)
        return message
    }


    /** **********************************************************************
     * response
     * Requestに対するResponse
     * ********************************************************************** */
    fun response(){
        observer = asyncStub.connect(object : StreamObserver<RoomMessage>{
            override fun onNext(value: RoomMessage) {
                Log.d("StreamObserver", "onNext: res -> ${value.toString()}")
                message = value
            }
            override fun onError(t: Throwable) {
                Log.d("StreamObserver", "onError: ${message.error.code}")

                when(message.error.code) {
                    0 -> Log.d("Error", "ErrInvalidToken")
                    1->  Log.d("Error", "ErrNotFound")
                    2 -> Log.d("Error", "ErrAlreadyExists")
                    3 -> Log.d("Error", "ErrHPisZero")
                    else -> Log.d("Error", "Internal")
                }
            }
            override fun onCompleted() {
                Log.d("StreamObserver", "onCompleted")
            }
        })
    }
}