package com.example.bolg

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import com.example.bolg.gameplay.GamePlayActivity
import com.example.bolg.standby.host.HostStandbyActivity
import com.example.bolg.standby.player.PlayerStandbyActivity
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import org.bolg_developers.bolg.*

/** ----------------------------------------------------------------------
 * GrpcTask
 * Singletonクラス
 * Serverとの通信を担う
 * ---------------------------------------------------------------------- */
class GrpcTask(application: Application)  {

    companion object{
        // コードに構造上の問題がないことを確認する
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: GrpcTask? = null
        fun getInstance(application: Application): GrpcTask {
            if(INSTANCE == null){
                INSTANCE = GrpcTask(application)
            }
            return INSTANCE!!
        }
    }

    // private val app = application

    /** bidirectional streaming RPC init **/
    private var observer: StreamObserver<RoomMessage>? = null
    private var message: RoomMessage? = null
    private var channel: ManagedChannel
    private val asyncStub: BolgServiceGrpc.BolgServiceStub

    init {
        Log.d("GrpcTask", "init")
        channel = ManagedChannelBuilder
            .forAddress("host", 0)
            .usePlaintext()
            .build()
        asyncStub = BolgServiceGrpc.newStub(channel)
    }

    /** SharedPreferenceのインスタンス生成 **/
    val data: SharedPreferences = application.getSharedPreferences("RoomDataSave", android.content.Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor? = data.edit()

    /** **********************************************************************
     * createAndJoinRoomTask
     * 部屋を生成して入室する
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun createAndJoinRoomTask(view: View){
        val createAndJoinRoomReqMsg: CreateAndJoinRoomRequest = CreateAndJoinRoomRequest.newBuilder().setPlayerName("HAL").build()
        message = RoomMessage.newBuilder().setCreateAndJoinRoomReq(createAndJoinRoomReqMsg).build()
        responseObserver(view)
        observer?.onNext(message)
    }

    /** **********************************************************************
     * joinRoomTask
     * @param roomId 部屋ID
     * @param view View
     * 部屋を生成して入室する
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun joinRoomTask(roomId: Long,view: View){
        val joinRoomReqMsg: JoinRoomRequest = JoinRoomRequest.newBuilder().setRoomId(roomId).setPlayerName("OSAKA").build()
        message = RoomMessage.newBuilder().setJoinRoomReq(joinRoomReqMsg).build()
        responseObserver(view)
        observer?.onNext(message)
    }

    /** **********************************************************************
     * startGame
     * @param token プレイヤー情報
     * ********************************************************************** */
    fun startGame(token: String?, view: View?) {
        val startGameReqMsg: StartGameRequest = StartGameRequest.newBuilder().setToken(token).build()
        message = RoomMessage.newBuilder().setStartGameReq(startGameReqMsg).build()
        responseObserver(view)
        observer?.onNext(message)
    }

    /** **********************************************************************
     * startGupdateWeaponame
     * @param attack 攻撃力
     * @param token トークン
     * ********************************************************************** */
    fun updateWeapon(attack: Long, token: String?, view: View?) {
        val updateWeaponReqMsg: UpdateWeaponRequest = UpdateWeaponRequest.newBuilder().setAttack(attack).setToken(token).build()
        message = RoomMessage.newBuilder().setUpdateWeaponReq(updateWeaponReqMsg).build()
        responseObserver(view)
        observer?.onNext(message)
    }

    /** **********************************************************************
     * setReady
     * @param token トークン
     * ********************************************************************** */
    fun setReady(token: String?, view: View?){
        val setReadyReqMsg: ReadyRequest = ReadyRequest.newBuilder().setToken(token).build()
        message = RoomMessage.newBuilder().setReadyReq(setReadyReqMsg).build()
        responseObserver(view)
        observer?.onNext(message)
        observer?.onCompleted()
    }

    /** **********************************************************************
     * inventory
     * @param token トークン
     * ********************************************************************** */
    fun inventory(token: String) {
    }

    /** **********************************************************************
     * responseObserver
     * @param view View
     * Requestに対してのResponseのハンドリング
     * @author 長谷川　勇太
     * ********************************************************************** */
    private fun responseObserver(view: View?){
        observer = asyncStub.connect(object : StreamObserver<RoomMessage>{
            override fun onNext(value: RoomMessage) {
                when(value.dataCase.number){
                    1 -> {
                        Log.d("GrpcTask", "joinCreate_and_join_room_req ->${value.createAndJoinRoomReq}")
                    }
                    2 -> {    // RoomMessage.create_and_join_room_resp
                        Log.d("GrpcTask","create_and_join_room_resp -> ${value.createAndJoinRoomResp}")

                        for(i in value.createAndJoinRoomResp.room.playersList){
                            Log.d("GrpcTask","create_and_join_room_resp:ready -> ${i.ready}")
                        }

                        if(value.createAndJoinRoomResp.room.id != 0L) {
                            Log.d("GrpcTask","no roomId 0")
                            // Player Info Save
                            editor?.putLong("room_id", value.createAndJoinRoomResp.room.id)
                            editor?.putLong("player_id", value.createAndJoinRoomResp.room.getPlayers(0).id)
                            editor?.putLong("player_hp", value.createAndJoinRoomResp.room.getPlayers(0).hp)
                            editor?.putString("player_name", value.createAndJoinRoomResp.room.getPlayers(0).name)
                            editor?.putBoolean("player_ready", value.createAndJoinRoomResp.room.getPlayers(0).ready)
                            editor?.putLong("owner_id", value.createAndJoinRoomResp.room.ownerId)
                            editor?.putInt("game_rule", value.createAndJoinRoomResp.room.gameRule.number)
                            editor?.putBoolean("game_start", value.createAndJoinRoomResp.room.gameStart)
                            editor?.putString("token", value.createAndJoinRoomResp.token)
                            editor?.apply()
                            val intent = Intent(view?.context, HostStandbyActivity::class.java)
                            view?.context?.startActivity(intent)
                        }
                    }
                    3 -> {
                        Log.d("GrpcTask", "join_room_req  ->${value.joinRoomReq}")
                    }
                    4 -> {    // RoomMessage.join_room_resp
                        Log.d("GrpcTask","join_room_resp -> ${value.joinRoomResp}")

                        if(value.joinRoomResp.room.id != 0L) {

                            // Player Info Save
                            editor?.putLong("room_id", value.joinRoomResp.room.id)
                            editor?.putLong("player_id", value.joinRoomResp.room.getPlayers(1).id)
                            editor?.putLong("player_hp", value.joinRoomResp.room.getPlayers(1).hp)
                            editor?.putBoolean("player_ready", value.joinRoomResp.room.getPlayers(1).ready)
                            editor?.putLong("owner_id", value.joinRoomResp.room.ownerId)
                            editor?.putInt("game_rule", value.joinRoomResp.room.gameRule.number)
                            editor?.putBoolean("game_start", value.joinRoomResp.room.gameStart)
                            editor?.putString("player_name", value.joinRoomResp.room.getPlayers(1).name)
                            editor?.putString("token", value.joinRoomResp.token)
                            editor?.apply()

                            // PlayerStandby Transition
                            Log.d("GrpcTask","join_room_respIntentStart")
                            val intent = Intent(view?.context, PlayerStandbyActivity::class.java)
                            view?.context?.startActivity(intent)
                        }
                    }
                    5 -> {
                        Log.d("GrpcTask", "join_room_msg  ->${value.joinRoomMsg}")
                        Log.d("GrpcTask", "player : ${value.joinRoomMsg.player.name} が入室しました。")
                    }
                    6 -> {
                        Log.d("GrpcTask", "notify_receiving_req ->${value.joinRoomMsg}")
                    }
                    7 -> {
                        Log.d("GrpcTask", "notify_receiving_msg ->${value.notifyReceivingMsg}")
                        Log.d("GrpcTask", "ダメージをうけたプレイヤー : ${value.notifyReceivingMsg.player.name}")
                    }
                    8 -> {
                        Log.d("GrpcTask", "survival_result_msg ->${value.survivalResultMsg}")
                        Log.d("GrpcTask", "${value.survivalResultMsg.winner.name}が勝利")
                    }
                    9 -> {
                        Log.d("GrpcTask", "start_game_req  ->${value.startGameReq}")
                    }
                    10 -> {    // StartGameMessage
                        Log.d("GrpcTask", "start_game_msg -> ${value.startGameMsg}")
                        Log.d("GrpcTask","げーむ開始")
                        val intent = Intent(view?.context, GamePlayActivity::class.java)
                        view?.context?.startActivity(intent)
                    }
                    11 -> {
                        Log.d("GrpcTask", "update_weapon_req   ->${value.updateWeaponReq}")
                    }
                    12 -> {
                        Log.d("GrpcTask", "update_weapon_resp -> 何もかえって来なくていい")
                    }
                    13 -> {
                        Log.d("GrpcTask", "ready_req  ->${value.updateWeaponResp}")
                    }
                    14 -> {    // ready_msg
                        Log.d("GrpcTask", "ready_msg  -> ${value.readyMsg}")
                        Log.d("GrpcTask", "playerID: ${value.readyMsg.playerId}が準備完了です")
                    }
                    15 -> {
                        when (value.error.message) {
                            "game is already starting" -> {
                                val intent = Intent(view?.context, GamePlayActivity::class.java)
                                view?.context?.startActivity(intent)
                                Log.d("GrpcTask", "ReadyMessage ->${value.error.message}")
                            }
                            else -> {
                                Log.d("GrpcTask", "ReadyMessage -> ${value.error.message}")
                            }
                        }
                        observer?.onCompleted()
                    }
                }
            }
            override fun onError(t: Throwable) {
                Log.d("GrpcTask", "onError: ${t.message}")
                Log.d("GrpcTask", "onError: ${message.toString()}")

                when(message?.error?.code) {
                    0 ->    { Log.d("GrpcTask", "onError/InvalidToken") }
                    1 ->    { Log.d("GrpcTask", "onError/NotFound") }
                    2 ->    { Log.d("GrpcTask", "onError/AlreadyExists") }
                    3 ->    { Log.d("GrpcTask", "onError/HPisZero") }
                    else -> { Log.d("GrpcTask", "onError/Internal") }
                }
            }
            override fun onCompleted() {
                Log.d("GrpcTask", "onCompleted")
            }
        })
    }
}