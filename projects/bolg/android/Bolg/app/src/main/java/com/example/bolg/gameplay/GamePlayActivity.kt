package com.example.bolg.gameplay

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.bolg.R
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.main.MainViewModel
import com.example.bolg.main.MainViewModelFactory

/** ----------------------------------------------------------------------
 * GamePlayActivity
 * ゲームプレイ中画面
 * ---------------------------------------------------------------------- */
class GamePlayActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)


        val application: Application = requireNotNull(this).application
        val viewModelFactory = GamePlayViewModelFactory(application)
        val gamePlayViewModel:GamePlayViewModel = ViewModelProviders.of(this,viewModelFactory).get(GamePlayViewModel::class.java)

        BluetoothFunction.getInstance().connect()

        // Bluetooth　メッセージ受信
        val bluetoothFunction = BluetoothFunction
        bluetoothFunction.getInstance().readByteArray.observe(this , Observer { read ->
            Log.d("GamePlayActivity" , "Bluetooth read ByteArray")
            gamePlayViewModel.readByte(read)
        })
    }


    /** **********************************************************************
     * onStart
     * ・Bluetoothデバイスに接続
     * ********************************************************************** */
    public override fun onStart() {
        super.onStart()
        Log.d("HostStandbyActivity", "onStart")
        BluetoothFunction.getInstance().connect()
    }

    /** **********************************************************************
     * onRestart
     * ・Bluetoothデバイスに接続
     * ********************************************************************** */
    public override fun onRestart() {
        super.onRestart()
        Log.d("HostStandbyActivity", "onRestart")
        BluetoothFunction.getInstance().connect()
    }

    /** **********************************************************************
     * onResume
     * ・Bluetoothデバイスに接続
     * ********************************************************************** */
    override fun onResume() {
        super.onResume()
        Log.d("HostStandbyActivity", "onResume")
        BluetoothFunction.getInstance().connect()
    }


    /** **********************************************************************
     * onPause
     * ・Bluetoothデバイスを切断
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