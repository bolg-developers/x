package com.example.bolg

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.bolg.gameplay.GamePlayActivity
import com.example.bolg.standby.host.HostStandbyActivity
import com.example.bolg.standby.player.PlayerStandbyActivity
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.*
import org.bolg_developers.bolg.*

/** ----------------------------------------------------------------------
 * GrpcTask
 * Singletonクラス
 * Serverとの通信を担う
 * bidirectionalStreamingRPCを行う場合常にServerとChannelが接続されている状態。
 * なのでシングルトン処理を行う
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
@Suppress("UNUSED_CHANGED_VALUE")
class GrpcTask(application: Application)  {

    /** Singleton **/
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

    /** bidirectional streaming rpc init **/
    private var observer  : StreamObserver<RoomMessage>? = null
    private var message   : RoomMessage? = null
    private var channel   : ManagedChannel
    private val asyncStub : BolgServiceGrpc.BolgServiceStub

    // 参加者人数
    var joinUserNum : MutableLiveData<Long>                  = MutableLiveData(0)
    var hitFlg      : MutableLiveData<Long>                  = MutableLiveData(0)
    var gameEndFlg  : MutableLiveData<RoomMessage>           = MutableLiveData()
    var joinFlg     : MutableLiveData<String>                = MutableLiveData("")
    var hitName     : MutableLiveData<String>                = MutableLiveData("")
    var readyFlg    : MutableLiveData<Boolean>               = MutableLiveData(true)
    var gameStart   : MutableLiveData<Boolean>               = MutableLiveData(true)
    var userNameList: MutableLiveData<MutableList<String>>   = MutableLiveData()
    var gameUserNameList: MutableLiveData<MutableList<String>>   = MutableLiveData()

    /** coroutine init **/
    private var liveDataJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + liveDataJob)

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
     * ユーザー　ー＞　オーナープレイヤー
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
     * ユーザー　ー＞　プレイヤー(ノーマルプレイヤー)
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
     * @param token トークン
     * ゲーム開始する
     * 条件：
     * ・pairing完了である
     * ・部屋に入室しているプレイヤーが準備完了である
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun startGame(token: String?, view: View?) {
        val startGameReqMsg: StartGameRequest = StartGameRequest.newBuilder().setToken(token).build()
        message = RoomMessage.newBuilder().setStartGameReq(startGameReqMsg).build()
        responseObserver(view)
        observer?.onNext(message)
    }

    /** **********************************************************************
     * updateWeapon
     * @param attack 攻撃力
     * @param token トークン
     * 武器のセット
     * @author 長谷川　勇太
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
     * 準備完了メッセージ
     * @author 長谷川　勇太
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
     * @author 長谷川　勇太
     * ********************************************************************** */

    /** **********************************************************************
     * NotifyReceivingTask
     * @param token トークン
     * @param playerId プレイヤーID
     * @param view  View
     * 準備完了メッセージ
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun notifyReceivingTask(token: String?, playerId: Long, view: View?){
        val notifyReceivingReqMsg: NotifyReceivingRequest = NotifyReceivingRequest
            .newBuilder()
            .setToken(token)
            .setPlayerId(playerId)
            .build()

        message = RoomMessage.newBuilder().setNotifyReceivingReq(notifyReceivingReqMsg).build()
        responseObserver(view)
        observer?.onNext(message)
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
                    1 -> { Log.d("GrpcTask", "joinCreate_and_join_room_req ->${value.createAndJoinRoomReq}") }
                    2 -> {    // create_and_join_room_resp
                        Log.d("GrpcTask","create_and_join_room_resp -> ${value.createAndJoinRoomResp}")
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
                            editor?.putBoolean("standby_state",true)

                            // 部屋内の準備完了人数の取得
                            var num = 0L
                            val cnt = value.createAndJoinRoomResp.room.playersCount
                            for (i in 0 until cnt){
                                if(value.createAndJoinRoomResp.room.getPlayers(i).ready){
                                    num++
                                }
                            }
                            editor?.putLong("player_ready_num", num)
                            editor?.apply()
                            Log.d("player_ready_num","準備完了人数${num}")

                            uiScope.launch {
                                delay(100)
                                Log.d("GrpcTask", "参加人数は${joinUserNum.value}人です")
                                joinUserNum.value = cnt.toLong()
                                Log.d("GrpcTask", "参加人数は${joinUserNum.value}になりました")
                                // List add observe
                                joinFlg.value = value.createAndJoinRoomResp.room.getPlayers(0).name

                                val list = mutableListOf<String>()
                                for(i in 0 until cnt){
                                    list.add(value.createAndJoinRoomResp.room.getPlayers(i).name)
                                }
                                Log.d("List","create_and_join_room_resp->" +
                                        "\n" +
                                        "Grpc側" +
                                        "$list")
                                userNameList.value = list
                            }

                            val intent = Intent(view?.context, HostStandbyActivity::class.java)
                            view?.context?.startActivity(intent)
                        }
                    }
                    3 -> { Log.d("GrpcTask", "join_room_req  ->${value.joinRoomReq}") }
                    4 -> {    // join_room_resp
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
                            editor?.putBoolean("standby_state",false)
                            editor?.apply()

                            // 部屋内の準備完了人数の取得
                            var num = 0L
                            val cnt = value.joinRoomResp.room.playersCount
                            for (i in 0 until cnt){
                                if(value.joinRoomResp.room.getPlayers(i).ready){
                                    num++
                                }
                            }
                            editor?.putLong("player_ready_num", num)
                            editor?.apply()
                            Log.d("player_ready_num","準備完了人数${num}")
                            // PlayerStandby Transition

                            uiScope.launch {
                                val list = mutableListOf<String>()
                                for(i in 0 until cnt){
                                    list.add(value.joinRoomResp.room.getPlayers(i).name)
                                }
                                Log.d("List","join_room_resp->" +
                                        "\n" +
                                        "Grpc側" +
                                        "$list")
                                userNameList.value = list

                                delay(600)
                                val intent = Intent(view?.context, PlayerStandbyActivity::class.java)
                                view?.context?.startActivity(intent)
                            }
                        }
                    }
                    5 -> {    // join_room_msg
                        Log.d("GrpcTask", "join_room_msg  ->${value.joinRoomMsg}")
                        Log.d("GrpcTask", "player : ${value.joinRoomMsg.player.name} が入室しました。")
                        Log.d("GrpcTask", "player : ${value.joinRoomMsg.player.id} が入室しました。")
                        editor?.putLong("test",value.joinRoomMsg.player.id)
                        editor?.apply()
                        uiScope.launch {
                            delay(100)
                            Log.d("GrpcTask", "参加人数は${joinUserNum.value}人です")
                            joinUserNum.value = joinUserNum.value?.plus(1)
                            Log.d("GrpcTask", "参加人数は${joinUserNum.value}になりました")

                            // List add observe
                            // joinFlg.value = value.joinRoomMsg.player.name
                            val list = userNameList.value
                            Log.d("List","join_room_msg->" +
                                    "\n" +
                                    "Grpc側：入室" +
                                    "$list")
                            for(i in 0 until list?.size!!) {
                                if(value.joinRoomMsg.player.name != list[i]) {
                                    list.add(value.joinRoomMsg.player.name)
                                }
                            }
                            Log.d("List","join_room_msg->" +
                                    "\n" +
                                    "Grpc側：入室処理後" +
                                    "$list")
                            userNameList.value = list
                        }

                    }
                    6 -> { Log.d("GrpcTask", "notify_receiving_req ->${value.joinRoomMsg}") }
                    7 -> {    // notify_receiving_msg
                        Log.d("GrpcTask", "notify_receiving_msg ->${value.notifyReceivingMsg}")
                        uiScope.launch {
                            delay(100)
//                            Toast.makeText(view?.context, "ダメージをうけたプレイヤー（ID） : ${value.notifyReceivingMsg.player.name}(${value.notifyReceivingMsg.player.id})", Toast.LENGTH_SHORT).show()
                            hitName.value = value.notifyReceivingMsg.player.name

                            if (data.getLong("player_id",999) == value.notifyReceivingMsg.player.id) {
                                hitFlg.value = value.notifyReceivingMsg.player.hp
                            }
                        }
                        Log.d("GrpcTask", "ダメージをうけたプレイヤー : ${value.notifyReceivingMsg.player.name}")
                        Log.d("GrpcTask", "ダメージをうけたプレイヤーid : ${value.notifyReceivingMsg.player.id}")

                    }
                    8 -> {    // survival_result_msg
                        Log.d("GrpcTask", "survival_result_msg ->${value.survivalResultMsg}")
                        Log.d("GrpcTask", "${value.survivalResultMsg.winner.name}が勝利")
                        editor?.putLong("player_ready_num",value.survivalResultMsg.personalsCount.toLong())
                        editor?.apply()
                        uiScope.launch {
                            delay(100)
                            gameEndFlg.value = value
                        }
                    }
                    9 -> { Log.d("GrpcTask", "start_game_req  ->${value.startGameReq}") }
                    10 -> {    // start_game_msg
                        Log.d("GrpcTask", "start_game_msg -> ${value.startGameMsg}")
                        Log.d("GrpcTask","げーむ開始")

                        // 部屋にいるすべてのプレイヤーが準備完了でゲームプレイ画面へ遷移する。

                        uiScope.launch {
                            delay(100)
                            gameStart.value = true

                            val list = mutableListOf<String>()
                            for(i in 0 until value.startGameMsg.room.playersCount){
                                list.add(value.startGameMsg.room.getPlayers(i).name)
                            }

                            gameUserNameList.value = list
                        }
                    }
                    11 -> { Log.d("GrpcTask", "update_weapon_req   ->${value.updateWeaponReq}") }
                    12 -> { Log.d("GrpcTask", "update_weapon_resp -> 何もかえって来なくていい") }
                    13 -> { Log.d("GrpcTask", "ready_req  ->${value.updateWeaponResp}") }
                    14 -> {    // ready_msg
                        Log.d("GrpcTask", "ready_msg  -> ${value.readyMsg}")
                        Log.d("GrpcTask", "playerID: ${value.readyMsg.playerId}が準備完了です")
                        // Testのため保存
                        editor?.putLong("test_player_id", value.readyMsg.playerId)
                        editor?.apply()
                        Log.d("player_ready_num", "save")

                        uiScope.launch {
                            var tmp = data.getLong("player_ready_num",99)
                            editor?.putLong("player_ready_num",++tmp)
                            editor?.apply()
                            Log.d("player_ready_num", "$tmp")
                            readyFlg.value = true
                            Log.d("player_ready_num", "observe before")
                        }
                    }
                    15 -> {    // error
                        when (value.error.message) {
                            "game is already starting" -> { // すでにゲームが開始している
                                val intent = Intent(view?.context, GamePlayActivity::class.java)
                                view?.context?.startActivity(intent)
                                Log.d("GrpcTask", "ReadyMessage ->${value.error.message}")
                            }
                            else -> {
                                Log.d("GrpcTask", "ReadyMessage -> ${value.error.message}")
                            }
                        }
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