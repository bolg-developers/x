package com.example.bolg.gameplay

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.bolg.R
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.main.MainViewModel
import com.example.bolg.main.MainViewModelFactory
import java.nio.ByteBuffer

/** ----------------------------------------------------------------------
 * GamePlayActivity
 * ゲームプレイ中画面
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class GamePlayActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        // root view
        val decorView = window.decorView

        val application: Application = requireNotNull(this).application
        val viewModelFactory = GamePlayViewModelFactory(application)
        val gamePlayViewModel:GamePlayViewModel = ViewModelProviders.of(this,viewModelFactory).get(GamePlayViewModel::class.java)

        /** SharedPreferences **/
        val data: SharedPreferences = getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)
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
            Log.d("createAndJoinRoomTask","Errorororor")
        }

        // Observe : 弾を撃った時に動く
        BluetoothFunction.getInstance().shootByteArray.observe(this , Observer { readByte ->
            Log.d("GamePlayActivity" , "Bluetooth read ByteArray")
            // Bluetoothの値GamePlayViewModelへ送る
            gamePlayViewModel.btShootRead(readByte)
        })

        // Observe : 弾を被弾時に動く
        BluetoothFunction.getInstance().hitByteArray.observe(this , Observer { readByte ->
            Log.d("GamePlayActivity" , "Bluetooth read ByteArray")
            // Bluetoothの値GamePlayViewModelへ送る
            gamePlayViewModel.btHitRead(readByte, decorView)
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