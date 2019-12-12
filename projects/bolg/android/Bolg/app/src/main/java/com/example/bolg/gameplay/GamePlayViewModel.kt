package com.example.bolg.gameplay

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.bolg.bluetooth.BluetoothFunction

/** ----------------------------------------------------------------------
 * GamePlayViewModel
 * @param application : AndroidViewModelの引数に使用
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class GamePlayViewModel(application: Application) : AndroidViewModel(application){
    companion object {
        // 定数
        private const val START_BYTE: Byte  = 0xfe.toByte()  // Bluetoothのスタートバイト
        private const val END_BYTE: Byte    = 0xff.toByte()  // Bluetoothのエンドバイト
        private const val BT_BUFFER_SIZE: Int = 16           // Bluetoothのバッファーサイズ
    }
    private var mShootReadByte = ByteArray(BT_BUFFER_SIZE)  // 撃った時にReadした値を格納
    private var mHitReadByte = ByteArray(BT_BUFFER_SIZE)    // 撃たれた時にReadした値を格納

    private var mWriteByte = ByteArray(BT_BUFFER_SIZE)  // Bluetoothへ送る値を格納

    /** **********************************************************************
     * btWriteByte
     * @param writeByte
     * ・引数のByteArrayをBluetoothへ送信する
     * @author 中田　桂介
     * ********************************************************************** */
    fun btWriteByte(writeByte: ByteArray){
        Log.d("GamePlayViewModel", "Write ByteArray")
        BluetoothFunction.getInstance().write(writeByte)
    }

    /** **********************************************************************
     * btShootRead
     * @param readByte
     * ・引数のByteArrayをbtShootReadに格納する
     * @author 中田　桂介
     * ********************************************************************** */
    fun btShootRead(readByte: ByteArray){
        Log.d("GamePlayViewModel", "Get ByteArray")
        mShootReadByte = readByte
    }

    /** **********************************************************************
     * btHitRead
     * @param readByte
     * ・引数のByteArrayをmHitReadByteに格納する
     * @author 中田　桂介
     * ********************************************************************** */
    fun btHitRead(readByte: ByteArray){
        Log.d("GamePlayViewModel", "Get ByteArray")
        mHitReadByte = readByte
    }

}