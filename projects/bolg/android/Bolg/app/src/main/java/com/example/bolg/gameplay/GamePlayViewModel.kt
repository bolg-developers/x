package com.example.bolg.gameplay

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.example.bolg.GrpcTask
import com.example.bolg.bluetooth.BluetoothFunction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.experimental.and

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
    private val app = application

    /** Coroutine定義 **/
    // Job Set
    private var viewModelJob = Job()
    // Scope Set
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /** SharedPreferenceのインスタンス生成 **/
    val data: SharedPreferences = app.getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)


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
    fun btHitRead(readByte: ByteArray, view: View){
        mHitReadByte = readByte

        Log.d("GamePlayViewModel", "btHitRead:readByte[0]->${mHitReadByte[0]}")
        Log.d("GamePlayViewModel", "btHitRead:readByte[1]->${mHitReadByte[1]}")
        Log.d("GamePlayViewModel", "btHitRead:readByte[2]->${mHitReadByte[2]}")
        Log.d("GamePlayViewModel", "btHitRead:readByte[3]->${mHitReadByte[3]}")
        Log.d("GamePlayViewModel", "btHitRead:readByte[4]->${mHitReadByte[4]}")
        Log.d("GamePlayViewModel", "btHitRead:readByte[5]->${mHitReadByte[5]}")
        Log.d("GamePlayViewModel", "btHitRead:readByte[6]->${mHitReadByte[6]}")

        // playerIDを抽出
        var playerId: Long = 0
        for (i in 2..5) {
            playerId = playerId shl 8
            playerId = playerId or (mHitReadByte[i] and 0xFF.toByte()).toLong()
        }
        Log.d("GamePlayViewModel", "btHitRead:playerId->${playerId}")

        // tokenの取得
        val token: String? = data.getString("token", "error")
        Log.d("GamePlayViewModel", "btHitRead:token->${token}")

        //  NotifyReceivingRequestを行う
        uiScope.launch {
            GrpcTask.getInstance(app).notifyReceivingTask(token, playerId, view)
        }
    }
}