package com.example.bolg.gameplay

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.GrpcTask
import com.example.bolg.R
import com.example.bolg.adapter.StandbyRecyclerAdapter
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.data.ListData
import kotlinx.android.synthetic.main.activity_game_play.*
import java.nio.ByteBuffer

/** ----------------------------------------------------------------------
 * GamePlayActivity
 * ゲームプレイ中画面
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
@Suppress("NAME_SHADOWING", "UNREACHABLE_CODE")
class GamePlayActivity : AppCompatActivity(){
    private var listFlg = false

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)
        BluetoothFunction.getInstance().connect()

        var hitCnt = 0
        var hitNameFlg = false

        // root view
        val decorView = window.decorView
        // hide navigation bar, hide status bar
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE

        val application: Application = requireNotNull(this).application
        val viewModelFactory = GamePlayViewModelFactory(application)
        val gamePlayViewModel:GamePlayViewModel =
            ViewModelProviders.of(this,viewModelFactory).get(GamePlayViewModel::class.java)

        /** widget init **/
        val playerHp : TextView = findViewById(R.id.hp)
        val joinUser : RecyclerView = findViewById(R.id.list)

        /** SharedPreferences **/
        val data: SharedPreferences = getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = data.edit()

        editor?.putBoolean("end_game",false)
        editor?.apply()

        playerHp.text = 100.toString()

        /** Toolbar init **/
        game_toolbar.title = data.getString("player_name","player_name_none") + data.getString("token","player_name_none")

        // playerIdをByteArrayに変換する
        val playerId = data.getLong("player_id",0)
        Log.d("GamePlayActivity", playerId.toString())
        val value: Int = playerId.toInt()
        val bytes = ByteBuffer.allocate(4).putInt(value).array()
        Log.d("createAndJoinRoomTask","mTempBuffer ->${bytes[0]} , ${bytes[1]} , ${bytes[2]} , ${bytes[3]} ")

        // send byteArray create
        val integers = byteArrayOf(
            0xfe.toByte(),    // StartByte
            0x00.toByte(),    // CommandByte
            bytes[0],         // DataByteArray
            bytes[1],
            bytes[2],
            bytes[3],
            0xff.toByte()    //  EndByte
        )
        Log.d("createAndJoinRoomTask","SENDBuffer ->${integers[0]} , ${integers[1]} , ${integers[2]} , ${integers[3]}, ${integers[4]} , ${integers[5]} , ${integers[6]} ")

        if(!BluetoothFunction.getInstance().write(integers)){
            Log.d("createAndJoinRoomTask","writeError")
        }

        // Observe : 弾を撃った時に動く
        BluetoothFunction.getInstance().shootByteArray.observe(this , Observer { readByte ->
            Log.d("GamePlayActivity" , "Bluetooth read ByteArray")
            // Bluetoothの値GamePlayViewModelへ送る
            gamePlayViewModel.btShootRead(readByte)
        })

        // Observe : 弾を被弾時に動く
        BluetoothFunction.getInstance().hitByteArray.observe(this , Observer { readByte ->
            //  一回目はスルーする
            if(hitCnt != 0) {
                Log.d("GamePlayActivityHit", "Bluetooth read ByteArray")
                // Bluetoothの値GamePlayViewModelへ送る
                gamePlayViewModel.btHitRead(readByte, decorView)
            }
        })

        /** Observe kind **/
        // HP更新
        GrpcTask.getInstance(application).hitFlg.observe(this, Observer { hp->
            Log.d("GamePlayActivity" , "HP Update")
            if(hitCnt > 0) {
                playerHp.text = hp.toString()
            }
            hitCnt++
        })

        GrpcTask.getInstance(application).gameEndFlg.observe(this, Observer {result->
            Log.d("GamePlayActivity" , "game end")
            if(data.getBoolean("end_game",false)) {
                AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                    .setTitle("リザルト")
                    .setMessage(
                        "勝者 \n" +
                                result.survivalResultMsg.winner.name + "\n"
                                + "参加者(死んだ回数) \n" +
                                result.survivalResultMsg.personalsOrBuilderList[0].playerName +
                                " (${result.survivalResultMsg.personalsOrBuilderList[0].killCount}回)" +
                                result.survivalResultMsg.personalsOrBuilderList[1].playerName +
                                " (${result.survivalResultMsg.personalsOrBuilderList[1].killCount}回)"
                    )
                    .setPositiveButton("もう一度") { _, _ ->
                        editor?.putBoolean("loop_state", true)
                        editor?.putBoolean("retry_state", false)
                        editor?.commit()
                        finish()
                    }
                    .setNegativeButton("終了") { _, _ ->
                        editor?.putBoolean("loop_state", true)
                        editor?.putBoolean("retry_state", true)
                        editor?.commit()
                        finish()
                    }
                    .show()
            }
        })

        // 被弾者通知
        GrpcTask.getInstance(application).hitName.observe(this, Observer { name->
            editor?.putBoolean("end_game",true)
            editor?.apply()
            if(hitNameFlg) {
                log.text = name + "が撃たれました！"
            }
            hitNameFlg = true
        })

        GrpcTask.getInstance(application).gameUserNameList.observe(this, Observer { joinUserList->
                Log.d("GameList","来た->${joinUserList}")
                val sampleList: MutableList<ListData> = mutableListOf()
                for (i in 0 until joinUserList.size) {
                    sampleList.add(ListData(joinUserList[i]))
                }
                val mAdapter = StandbyRecyclerAdapter(sampleList)
                joinUser.adapter = mAdapter
                // 区切り線の表示
                joinUser.addItemDecoration(
                    DividerItemDecoration(
                        applicationContext,
                        DividerItemDecoration.VERTICAL
                    )
                )
        })

    }
    // ↓ここから下はBluetoothのconnect、disconnectをしているだけ
    /** **********************************************************************
     * onStart
     * ・Bluetoothデバイスに接続
     * @author 中田　桂介
     * ********************************************************************** */
    public override fun onStart() {
        super.onStart()
        Log.d("HostStandbyActivity", "onStart")
        BluetoothFunction.getInstance().connect()
    }
    /** **********************************************************************
     * onRestart
     * ・Bluetoothデバイスに接続
     * @author 中田　桂介
     * ********************************************************************** */
    public override fun onRestart() {
        super.onRestart()
        Log.d("HostStandbyActivity", "onRestart")
//        BluetoothFunction.getInstance().connect()
    }
    /** **********************************************************************
     * onResume
     * ・Bluetoothデバイスに接続
     * @author 中田　桂介
     * ********************************************************************** */
    override fun onResume() {
        super.onResume()
        Log.d("HostStandbyActivity", "onResume")
//        BluetoothFunction.getInstance().connect()
    }
    /** **********************************************************************
     * onPause
     * ・Bluetoothデバイスを切断
     * @author 中田　桂介
     * ********************************************************************** */
    public override fun onPause() {
        super.onPause()  // Always call the superclass method first
        Log.d("HostStandbyActivity", "onPause")
//        if (null != BluetoothFunction.getInstance().mBluetoothService) {
//            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
//            BluetoothFunction.getInstance().mBluetoothService = null
//        }
    }
    /** **********************************************************************
     * onStop
     * ・Bluetoothデバイスを切断
     * @author 中田　桂介
     * ********************************************************************** */
    public override fun onStop() {
        super.onStop()
        Log.d("HostStandbyActivity", "onStop")
//        if (null != BluetoothFunction.getInstance().mBluetoothService) {
//            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
//            BluetoothFunction.getInstance().mBluetoothService = null
//        }
    }
    /** **********************************************************************
     * onDestroy
     * ・Bluetoothデバイスを切断
     * @author 中田　桂介
     * ********************************************************************** */
    override fun onDestroy() {
        super.onDestroy()
        Log.d("HostStandbyActivity", "onDestroy")
//        if (null != BluetoothFunction.getInstance().mBluetoothService) {
//            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
//            BluetoothFunction.getInstance().mBluetoothService = null
//        }
    }
}