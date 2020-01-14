package com.example.bolg.gameplay

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.GrpcTask
import com.example.bolg.R
import com.example.bolg.adapter.StandbyRecyclerAdapter
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.data.ListData
import com.example.bolg.main.MainActivity
import com.example.bolg.standby.host.HostStandbyActivity
import com.example.bolg.standby.player.PlayerStandbyActivity
import kotlinx.android.synthetic.main.activity_host_standby.*
import kotlinx.coroutines.*
import java.nio.ByteBuffer

/** ----------------------------------------------------------------------
 * GamePlayActivity
 * ゲームプレイ中画面
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
@Suppress("NAME_SHADOWING")
class GamePlayActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        var hitCnt = 0

        // root view
        val decorView = window.decorView

        val application: Application = requireNotNull(this).application
        val viewModelFactory = GamePlayViewModelFactory(application)
        val gamePlayViewModel:GamePlayViewModel =
            ViewModelProviders.of(this,viewModelFactory).get(GamePlayViewModel::class.java)
        val playerHp: TextView = findViewById(R.id.hp)
        val joinUser          : RecyclerView = findViewById(R.id.list)


        /**************************ViewModelに分割したい*********************************************************/
        // LayoutManagerの設定
        val layoutManager = LinearLayoutManager(this)
        joinUser.layoutManager = layoutManager
        // Adapterの設定
        val sampleList: MutableList<ListData> = mutableListOf()
        for (i: Int in 0..10) { sampleList.add(i, ListData("参加ユーザー${i}")) }
        val adapter = StandbyRecyclerAdapter(sampleList)
        joinUser.adapter = adapter
        // 区切り線の表示
        joinUser.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        /******************************************************************************************************/


        /** SharedPreferences **/
        val data: SharedPreferences = getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)

//        playerHp.text = data.getLong("player_hp",0).toString()
        playerHp.text = 100.toString()

        // playerIdをByteArrayに変換する
        val playerId = data.getLong("player_id",0)
        val value: Int = playerId.toInt()

        Log.d("createAndJoinRoomTask", "playerId -> $value")
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
        Log.d("createAndJoinRoomTask","mTempBuffer ->${integers[0]} , ${integers[1]} , ${integers[2]} , ${integers[3]}, ${integers[4]} , ${integers[5]} , ${integers[6]} ")

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
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setTitle("リザルト")
                .setMessage(
                    "勝者:${result.survivalResultMsg.winner.name}"+ "\n"
                  + "参加者：${result.survivalResultMsg.personalsOrBuilderList[0]},${result.survivalResultMsg.personalsOrBuilderList[1]}"
                )
                .setPositiveButton("もう一度") { _, _ ->
                     val intent:Intent
                    if(data.getBoolean("standby_state",true)){
                        intent = Intent(this, HostStandbyActivity::class.java)
                    }else{
                        intent = Intent(this, PlayerStandbyActivity::class.java)
                    }
                    startActivity(intent)
                }
                .setNegativeButton("終了"){_, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .show()
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
        BluetoothFunction.getInstance().connect()
    }
    /** **********************************************************************
     * onResume
     * ・Bluetoothデバイスに接続
     * @author 中田　桂介
     * ********************************************************************** */
    override fun onResume() {
        super.onResume()
        Log.d("HostStandbyActivity", "onResume")
        BluetoothFunction.getInstance().connect()
    }
    /** **********************************************************************
     * onPause
     * ・Bluetoothデバイスを切断
     * @author 中田　桂介
     * ********************************************************************** */
    public override fun onPause() {
        super.onPause()  // Always call the superclass method first
        Log.d("HostStandbyActivity", "onPause")
        if (null != BluetoothFunction.getInstance().mBluetoothService) {
            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
            BluetoothFunction.getInstance().mBluetoothService = null
        }
    }
    /** **********************************************************************
     * onStop
     * ・Bluetoothデバイスを切断
     * @author 中田　桂介
     * ********************************************************************** */
    public override fun onStop() {
        super.onStop()
        Log.d("HostStandbyActivity", "onStop")
        if (null != BluetoothFunction.getInstance().mBluetoothService) {
            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
            BluetoothFunction.getInstance().mBluetoothService = null
        }
    }
    /** **********************************************************************
     * onDestroy
     * ・Bluetoothデバイスを切断
     * @author 中田　桂介
     * ********************************************************************** */
    override fun onDestroy() {
        super.onDestroy()
        Log.d("HostStandbyActivity", "onDestroy")
        if (null != BluetoothFunction.getInstance().mBluetoothService) {
            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
            BluetoothFunction.getInstance().mBluetoothService = null
        }
    }
}