package com.example.bolg.gameplay

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.GrpcTask
import com.example.bolg.R
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.main.MainActivity
import com.example.bolg.standby.host.HostStandbyActivity
import com.example.bolg.standby.player.PlayerStandbyActivity
import kotlinx.android.synthetic.main.activity_game_play.*
import java.nio.ByteBuffer

/** ----------------------------------------------------------------------
 * GamePlayActivity
 * ゲームプレイ中画面
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
@Suppress("NAME_SHADOWING", "UNREACHABLE_CODE")
class GamePlayActivity : AppCompatActivity(){
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        var hitCnt = 0
        var hitNameFlg = false
        var dialogFlg  = false

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

        gamePlayViewModel.updateList(this,joinUser, "参加者")

        /** SharedPreferences **/
        val data: SharedPreferences = getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)
        playerHp.text = 100.toString()
        /** Toolbar init **/
        game_toolbar.title = data.getString("player_name","player_name_none") + data.getString("token","player_name_none")

        // playerIdをByteArrayに変換する
        val playerId = data.getLong("player_id",0)
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
                        "勝者 \n" +
                                result.survivalResultMsg.winner.name + "\n"
                                + "参加者(死んだ回数) \n" +
                                result.survivalResultMsg.personalsOrBuilderList[0].playerName +
                                " (${result.survivalResultMsg.personalsOrBuilderList[0].killCount}回)" +
                                result.survivalResultMsg.personalsOrBuilderList[1].playerName +
                                " (${result.survivalResultMsg.personalsOrBuilderList[1].killCount}回)"
                    )
                    .setPositiveButton("もう一度") { _, _ ->
                        val intent: Intent = if (data.getBoolean("standby_state", true)) {
                            Intent(this, HostStandbyActivity::class.java)
                        } else {
                            Intent(this, PlayerStandbyActivity::class.java)
                        }
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("終了") { _, _ ->
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .show()
        })

        // 被弾者通知
        GrpcTask.getInstance(application).hitName.observe(this, Observer { name->
            if(hitNameFlg) {
                log.text = name + "が撃たれました！"
            }
            hitNameFlg = true
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