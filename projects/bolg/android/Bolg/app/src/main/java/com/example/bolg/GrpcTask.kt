package com.example.bolg

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import com.example.bolg.standby.host.HostStandbyActivity
import com.example.bolg.standby.player.PlayerStandbyActivity
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import org.bolg_developers.bolg.*

/** ----------------------------------------------------------------------
 * GrpcTask
 * Serverとの通信を担う
 * ---------------------------------------------------------------------- */
class GrpcTask(application: Application)  {

    private var channel: ManagedChannel
    private var blockingStub: BolgServiceGrpc.BolgServiceBlockingStub?
    private var asyncStub: BolgServiceGrpc.BolgServiceStub
    private lateinit var observer: StreamObserver<RoomMessage>
    private lateinit var message: RoomMessage
    private var intent: Intent? = null
    //  SharedPreferenceのインスタンス生成
    val data: SharedPreferences = application.getSharedPreferences("RoomDataSave", android.content.Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor? = data.edit()

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
     * @param view View
     * 部屋を生成して入室する
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun createAndJoinRoomTask(view: View){
        val reqMessageTest = CreateAndJoinRoomRequest.newBuilder().setPlayerName("HAL").build()
        message = RoomMessage.newBuilder().setCreateAndJoinRoomReq(reqMessageTest).build()
        responseObserver(view)
        observer.onNext(message)
        observer.onCompleted()
    }

    /** **********************************************************************
     * joinRoomTask
     * @param roomId 部屋ID
     * @param view View
     * 部屋を生成して入室する
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun joinRoomTask(roomId: Long,view: View){
        val reqMessage = JoinRoomRequest.newBuilder().setRoomId(roomId).setPlayerName("OSAKA").build()
        //val reqMessage = JoinRoomRequest.newBuilder().setRoomId(roomId).build()
        message = RoomMessage.newBuilder().setJoinRoomReq(reqMessage).build()
        responseObserver(view)
        observer.onNext(message)
        observer.onCompleted()
    }

    /** **********************************************************************
     * responseObserver
     * @param view View
     * Requestに対してのResponseのハンドリング
     * @author 長谷川　勇太
     * ********************************************************************** */
    private fun responseObserver(view: View){
        observer = asyncStub.connect(object : StreamObserver<RoomMessage>{
            override fun onNext(value: RoomMessage) {
                when(value.dataCase.number){
                    2 -> {    // RoomMessage.create_and_join_room_resp
                        Log.d("CreateJoinGrpcTask","create_and_join_room_resp -> ${value.createAndJoinRoomResp}")

                        if(value.createAndJoinRoomResp.room.id != 0L) {
                            Log.d("CreateJoinGrpcTask","no roomId 0")
                            editor?.putLong("room_id", value.createAndJoinRoomResp.room.id)
                            editor?.putLong("player_id", 1111)
                            editor?.putLong("player_hp", 100)
                            editor?.putBoolean("player_ready", true)
                            editor?.putLong("owner_id", value.createAndJoinRoomResp.room.ownerId)
                            editor?.putInt("game_rule", value.createAndJoinRoomResp.room.gameRule.number)
                            editor?.putBoolean("game_start", value.createAndJoinRoomResp.room.gameStart)
                            editor?.putString("token", value.createAndJoinRoomResp.token)
                            editor?.apply()

                            Log.d("CreateJoinGrpcTask","StandbyIntent")
                            intent = Intent(view.context, HostStandbyActivity::class.java)
                            view.context.startActivity(intent)
                        }
                    }

                    4 -> {    // RoomMessage.join_room_resp
                        Log.d("CreateJoinGrpcTask","join_room_resp -> ${value.joinRoomResp}")

                        if(value.joinRoomResp.room.id != 0L) {
                            editor?.putLong("room_id", value.joinRoomResp.room.id)
                            editor?.putLong("player_id", 3333)
                            editor?.putLong("player_hp", 100)
                            editor?.putBoolean("player_ready", true)
                            editor?.putLong("owner_id", value.joinRoomResp.room.ownerId)
                            editor?.putInt("game_rule", value.joinRoomResp.room.gameRule.number)
                            editor?.putBoolean("game_start", value.joinRoomResp.room.gameStart)
                            editor?.putString("player_name", "yuta")
                            editor?.putString("token", value.joinRoomResp.token)
                            editor?.apply()

                            intent = Intent(view.context, PlayerStandbyActivity::class.java)
                            view.context.startActivity(intent)
                        }
                    }
                    15 -> {    // RoomMessage.error
                        Log.d("CreateJoinGrpcTask","error -> ${value.error}")
                    }
                }
            }
            override fun onError(t: Throwable) {
                Log.d("CreateJoinGrpcTask", "onError: ${message.error.code}")
                Log.d("CreateJoinGrpcTask", "onError: ${t.message}")
                Log.d("CreateJoinGrpcTask", "onError: ${t.cause.toString()}")

                when(message.error.code) {
                    0 ->    { Log.d("CreateJoinGrpcTask", "onError/InvalidToken") }
                    1 ->    { Log.d("CreateJoinGrpcTask", "onError/NotFound") }
                    2 ->    { Log.d("CreateJoinGrpcTask", "onError/AlreadyExists") }
                    3 ->    { Log.d("CreateJoinGrpcTask", "onError/HPisZero") }
                    else -> { Log.d("CreateJoinGrpcTask", "onError/Internal") }
                }
            }
            override fun onCompleted() {
                Log.d("createAndJoinRoomTask", "onCompleted")
            }
        })
    }
}