package com.example.bolg.gameplay

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.GrpcTask
import com.example.bolg.adapter.StandbyRecyclerAdapter
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.data.ListData
import kotlinx.coroutines.*
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

    private var layoutManager: LinearLayoutManager? = null
    private val sampleList: MutableList<ListData> = mutableListOf()

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

        if(playerId != data.getLong("player_id",999)) {
            // tokenの取得
            val token: String? = data.getString("token", "error")
            Log.d("GamePlayViewModel", "btHitRead:token->${token}")

            //  NotifyReceivingRequestを行う
            uiScope.launch {
                delay(200)
                Log.d("GamePlayViewModel", "notifyReceivingTaskStart")
                Log.d("GamePlayViewModel", "token->${token},playerId->${playerId}")
                GrpcTask.getInstance(app).notifyReceivingTask(token, playerId, view)
            }
        }
    }

    /** **********************************************************************
     * updateList
     * @param context Context
     * @param joinUser
     * @param name
     * @author 長谷川　勇太
     * ********************************************************************** */
//    fun updateList(context: Context,joinUser:RecyclerView,name: String, genre: Int){
//        // LayoutManagerの設定
//        layoutManager = LinearLayoutManager(context)
//        joinUser.layoutManager = layoutManager
//        // Adapterの設定
//        Log.d("RecyclerList", "updateList->$sampleList")
//        if(genre == 0) {
//            if (sampleList.size > 0) {
//                sampleList.removeAt(0)
//                Log.d("RecyclerList", "remove")
//                Log.d("RecyclerList", "updateList->$sampleList")
//            }
//        }
//        sampleList.add(ListData(name))
//        Log.d("RecyclerList", "updateList->$sampleList")
//        val adapter = StandbyRecyclerAdapter(sampleList)
//        joinUser.adapter = adapter
//        // 区切り線の表示
//        joinUser.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//    }

}