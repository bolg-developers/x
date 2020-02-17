package com.example.bolg.bluetooth

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.bolg.MyApplication
import com.example.bolg.R
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/** ----------------------------------------------------------------------
 * BluetoothFunction
 * ・Bluetoothアダプタの取得
 * ・Bluetoothデバイスとペアリング、接続、切断
 * ・Bluetooth、マイコン間での送受信
 * @author 中田　桂介
 * ---------------------------------------------------------------------- */
class BluetoothFunction private constructor() {
    companion object {
        // 定数
        private const val REQUEST_ENABLEBLUETOOTH: Int  = 1  // Bluetooth機能の有効化要求時の識別コード
        private const val BT_BUFFER_SIZE: Int = 32           // Bluetooth受信バッファーサイズ
        private const val START_BYTE: Byte = 0xfe.toByte()   // Bluetoothのスタートバイト
        private const val END_BYTE: Byte = 0xff.toByte()     // Bluetoothのエンドバイト
        // Singleton
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: BluetoothFunction ? = null
        fun getInstance(): BluetoothFunction {
            if(INSTANCE == null){
                INSTANCE = BluetoothFunction()
            }
            return INSTANCE!!
        }
    }
    // Job Set
    private var viewModelJob = Job()
    // Scope Set
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // メンバー変数
    var mBluetoothService: BluetoothService? = null    // Bluetoothデバイスとの通信処理を担うクラス
    private var mBluetoothAdapter: BluetoothAdapter? = null     // BluetoothAdapter : Bluetooth処理で必要
    private var mDeviceAddress: String = ""                     // DeviceのAddress格納
    private var mReadBuffer = ByteArray(BT_BUFFER_SIZE)        // byte型で値が来る
    private var mReadTempBuffer = ByteArray(BT_BUFFER_SIZE)        // byte型で値が来る
    //private var mReadBufferCounter: Int = 0
    // Bluetoothから読み込んだ値が格納される
    var hitByteArray: MutableLiveData<ByteArray> = MutableLiveData(mReadBuffer)
    var shootByteArray: MutableLiveData<ByteArray> = MutableLiveData(mReadBuffer)
    var loopCnt = 0
    var cntTemp = 0
    // applicationContext取得用
    private val mGetContext = MyApplication
    var context: Context? = null
    // Bluetoothが接続されてるかの可否
    var mBluetoothEnable: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        // Bluetoothアダプタの取得
        val bluetoothManager: BluetoothManager = mGetContext.applicationContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
        // Android端末がBluetoothをサポートしていない場合アプリを終了する
        if( null == mBluetoothAdapter ) {
            Log.d("BluetoothFunction", "Can_not_get_BluetoothAdapter")
        }
        Log.d("BluetoothFunction", "Get_BluetoothAdapter")
    }

    /** **********************************************************************
     * btPairing
     * @return Boolean
     * ・Bluetoothデバイスとペアリングする
     * @author 中田　桂介
     * ********************************************************************** */
    fun btPairing(view: View?): Boolean {

        if( null == mBluetoothAdapter ) {
            Log.d("BluetoothFunction", "Can_not_get_BluetoothAdapter")
            return false
        }
        uiScope.launch {
            //ペアリング済みの端末セットの問い合わせ
            val pairedDevices: Set<BluetoothDevice>  = mBluetoothAdapter!!.bondedDevices
            val deviceItems = mutableListOf<String>()
            val deviceNameItems = mutableListOf<String>()
            if (pairedDevices.isNotEmpty()) {
                for (device in pairedDevices) {
                    //ペアリング済みのデバイス名とMACアドレスの表示
                    deviceItems.add(device.address.toString())
                    deviceNameItems.add(device.name.toString())
                    Log.d("BluetoothFunction", "DeviceName: " + device.name.toString())
                }
            }

            delay(500)
            AlertDialog.Builder(view?.context)
            .setTitle("Selector")
            .setItems(deviceNameItems.toTypedArray()) { _, which ->
                mDeviceAddress = deviceItems[which]
                // Bluetoothデバイスに接続
                connect()
            }
            .show()
        }

        return true
    }

    /** **********************************************************************
     * connect
     * @return Boolean
     * ・選択されたBluetoothデバイスに接続
     * @author 中田　桂介
     * ********************************************************************** */
    fun connect(): Boolean {
        // mDeviceAddressが空の場合は処理しない
        if (mDeviceAddress == "") {
            Toast.makeText(MyApplication.applicationContext(), R.string.notPaired, Toast.LENGTH_SHORT).show()
            return false
        }
        // mBluetoothServiceがnullでない場合「接続済み」or「接続中」
        if (null != mBluetoothService) {
            Toast.makeText(MyApplication.applicationContext(), R.string.btConnecting, Toast.LENGTH_SHORT).show()
            Log.d("BluetoothFunction", "Connecting")
            return false
        }

        val device: BluetoothDevice = mBluetoothAdapter!!.getRemoteDevice(mDeviceAddress)
        // BluetoothService: インスタンス生成
        mBluetoothService = BluetoothService(MyApplication.applicationContext(), mHandler, device)
        // Bluetooth接続開始処理
        mBluetoothService!!.connectStart()
        return true

    }

    /** **********************************************************************
     * disconnect
     * @return Boolean
     * ・選択されたBluetoothデバイス切断
     * @author 中田　桂介
     * ********************************************************************** */
    fun disconnect(): Boolean {
        // mBluetoothServiceがnullの場合「切断済み」or「切断中」
        if (null == mBluetoothService) {
            Log.d("BluetoothFunction", "Disconnected or Disconnecting")
            return false
        }

        Log.d("BluetoothService", "本当の切断")
        // Bluetooth接続終了処理
        mBluetoothService!!.disconnectStart()
        // インスタンス削除
        mBluetoothService = null
        return true
    }

    /** **********************************************************************
     * write
     * @param byteBuff
     * @return Boolean
     * ・byteArrayの送信
     * @author 中田　桂介
     * ********************************************************************** */
    fun write(byteBuff: ByteArray): Boolean {
        // mBluetoothServiceがnullの場合「切断済み」or「切断中」
        if (null == mBluetoothService) {
            Log.d("BluetoothFunction", "Disconnected or Disconnecting")
            return false
        }

        // バイト列送信
        if(!mBluetoothService!!.writePreparation(byteBuff)){
            Log.d("BluetoothFunction", "writePreparationError")
            return false
        }

        return true
    }

    /** **********************************************************************
     * readReadByteBuffer
     * @return ByteArray
     * ・読み込んだ文字列を返す
     * @author 中田　桂介
     * ********************************************************************** */
    fun readReadByteBuffer():ByteArray {
        return mReadBuffer
    }

    /** ----------------------------------------------------------------------
     * BluetoothService
     * ・概要1
     * ・概要2
     * @author 中田　桂介
     * ---------------------------------------------------------------------- */
    open class BluetoothService {
        // 定数
        companion object {
            // Bluetooth UUID
            private val UUID_SPP = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
            // State
            const val MESSAGE_STATECHANGE            = 1
            const val STATE_NONE                     = 0
            const val STATE_CONNECT_START            = 1
            const val STATE_CONNECT_FAILED           = 2
            const val STATE_CONNECTED                = 3
            const val STATE_CONNECTION_LOST          = 4
            const val STATE_DISCONNECT_START         = 5
            const val STATE_DISCONNECTED             = 6
            const val MESSAGE_READ                   = 7
            const val MESSAGE_WRITE                  = 8
        }
        // メンバー変数
        private var mState: Int = 0
        private var mHandler: Handler
        private var mConnectionThread: ConnectionThread? = null

        /** **********************************************************************
         * constructor(BluetoothService)
         * ・Bluetooth接続時処理用スレッドの作成と開始
         * @author 中田　桂介
         * ********************************************************************** */
        constructor(context: Context, handler: Handler, device: BluetoothDevice) {
            mHandler = handler
            mState = STATE_NONE

            Log.d("BluetoothService", "BluetoothService constructor")

            // 接続時処理用スレッドの作成と開始
            mConnectionThread = ConnectionThread(device)
            mConnectionThread!!.start()
        }

        /** **********************************************************************
         * setState
         * ・状態の変更、ハンドラーにメッセージを送る
         * @author 中田　桂介
         * ********************************************************************** */
        @Synchronized
        open fun setState(state: Int) {
            Log.d("BluetoothService", "SET_STATE:$state")

            mState = state
            mHandler.obtainMessage(MESSAGE_STATECHANGE, state, -1).sendToTarget()
            return
        }

        /** **********************************************************************
         * connectStart
         * ・Bluetoothデバイスに接続
         * @author 中田　桂介
         * ********************************************************************** */
        @Synchronized
        fun connectStart() {
            // 未接続なら処理をしない
            if (STATE_NONE != mState) {
                return
            }

            Toast.makeText(MyApplication.applicationContext(), R.string.btConnect, Toast.LENGTH_SHORT).show()
            Log.d("BluetoothService", "Connect")

            // ステータス設定
            setState(STATE_CONNECT_START)
        }

        /** **********************************************************************
         * disconnectStart
         * ・Bluetoothデバイス切断
         * @author 中田　桂介
         * ********************************************************************** */
        @Synchronized
        fun disconnectStart() {
            // 未接続なら処理をしない
            if (STATE_CONNECTED != mState) {
                return
            }

            Toast.makeText(MyApplication.applicationContext(), R.string.btDisconnect, Toast.LENGTH_SHORT).show()
            Log.d("BluetoothService", "Disconnect")

            // ステータス設定
            setState(STATE_DISCONNECT_START)
            // mBluetoothSocket colse
            mConnectionThread!!.cancel()
        }


        /** **********************************************************************
         * writePreparation
         * @param byteBuff
         * @return Boolean
         * ・スレッドを作成
         * @author 中田　桂介
         * ********************************************************************** */
        fun writePreparation(byteBuff: ByteArray): Boolean {
            // スレッド作成
            var connectionThread: ConnectionThread? = null
            synchronized(this) {
                if (STATE_CONNECTED != mState) {
                    Log.d("writePreparation", "STATE_CONNECTEDError")
                    return false
                }
                connectionThread = mConnectionThread
            }
            if(!connectionThread!!.writeStart(byteBuff)){
                Log.d("writePreparation", "writeStartError")
                return false
            }
            return true
        }

        /** ----------------------------------------------------------------------
         * ConnectionThread
         * @param Thread
         * ・ConnectionThread
         * @author 中田　桂介
         * ---------------------------------------------------------------------- */
        inner class ConnectionThread: Thread  {
            private var mBluetoothSocket: BluetoothSocket? = null       //  BluetoothDeviceを取得し、socketを取得する
            private var mInput: InputStream? = null     //メッセージinput
            private var mOutput: OutputStream? = null   //メッセージoutput

            /** **********************************************************************
             * constructor(ConnectionThread)
             * ・コンストラクタ
             * @author 中田　桂介
             * ********************************************************************** */
            constructor(bluetoothDevice: BluetoothDevice) {
                Log.d("Bluetooththread", "ConnectionThread　constructor")
                try {
                    mBluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID_SPP)
                    mInput = mBluetoothSocket!!.inputStream
                    mOutput = mBluetoothSocket!!.outputStream
                } catch (e: IOException) {
                    Log.e("BluetoothService", "failed : bluetoothdevice.createRfcommSocketToServiceRecord( UUID_SPP )", e)
                }
            }

            /** **********************************************************************
             * writeStart
             * @param byteBuff
             * @return Boolean
             * ・書き込み開始時処理
             * @author 中田　桂介
             * ********************************************************************** */
            fun writeStart(byteBuff: ByteArray): Boolean {
                try {
                    synchronized(this@BluetoothService) {
                        mOutput!!.write(byteBuff)
                        Log.d("Bluetooththread", "Write string")
                    }
                    mHandler.obtainMessage(MESSAGE_WRITE).sendToTarget()
                } catch (e: IOException) {
                    Log.e("BluetoothService", "Failed : mBluetoothSocket.close()", e)
                    return false
                }
                return true
            }

            /** **********************************************************************
             * run
             * ・Bluetooth処理
             * @author 中田　桂介
             * ********************************************************************** */
            override fun run() {
                while (STATE_DISCONNECTED != mState) {
                    when (mState) {
                        // 接続開始
                        STATE_CONNECT_START -> {
                            try {
                                Log.d("BluetoothService", "STATE_CONNECT_START")
                                // BluetoothSocketオブジェクトを用いて、Bluetoothデバイスに接続を試みる。
                                mBluetoothSocket!!.connect()
                            } catch (e: IOException) {    // 接続失敗
                                setState(STATE_CONNECT_FAILED)
                                Log.d("BluetoothService", "STATE_CONNECT_START：スレッド終了")
                                cancel()    // スレッド終了
                                return
                            }
                            // 接続成功
                            Log.d("BluetoothService", "STATE_CONNECT_START：OK")
                            setState(STATE_CONNECTED)
                        }
                        // 接続済み（Bluetoothデバイスから送信されるデータ受信）
                        STATE_CONNECTED -> {
                            Log.d("BluetoothService", "STATE_CONNECTED")
                            val buff = ByteArray(BT_BUFFER_SIZE)
                            val byte: Int
                            try {
                                // 文字列読み込み
                                byte = mInput!!.read(buff)
                                mHandler.obtainMessage(MESSAGE_READ, byte, -1,buff).sendToTarget()
                                Log.d("BluetoothService", "Read string")
                            } catch (e: IOException) {
                                Log.d("BluetoothService", "runスレッド終了")
                                setState(STATE_CONNECTION_LOST)
                                cancel()        // スレッド終了
                            }
                        }
                        // 未接続
                        STATE_NONE -> {
                        }
                        // 接続失敗
                        STATE_CONNECT_FAILED -> {
                            // 接続失敗時の処理の実体は、cancel()
                        }
                        // 接続ロスト
                        STATE_CONNECTION_LOST -> {
                            // 接続ロスト時の処理の実体は、cancel()
                        }
                        // 切断開始
                        STATE_DISCONNECT_START -> {
                            // 切断開始時の処理の実体は、cancel()
                        }
                    }
                }
                synchronized (BluetoothService) {
                    // 親クラスが保持する自スレッドオブジェクトの解放（自分自身の解放）
                    mConnectionThread = null
                }
            }

            /** **********************************************************************
             * cancel
             * ・接続終了
             * @author 中田　桂介
             * ********************************************************************** */
            fun cancel() {
                try {
                    mBluetoothSocket!!.close()
                } catch (e: IOException) {
                    Log.e("BluetoothService", "Failed : mBluetoothSocket.close()", e)
                }
                setState(STATE_DISCONNECTED)
            }

        }
    }

    /** **********************************************************************
     * Handler
     * ・Handler
     * @author 中田　桂介
     * ********************************************************************** */
    private var mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                BluetoothService.MESSAGE_STATECHANGE -> when (msg.arg1) {
                    // 未接続
                    BluetoothService.STATE_NONE -> {
                        Log.d("Bluetoothhandle", "Disconnected")
                    }
                    // 接続開始
                    BluetoothService.STATE_CONNECT_START -> {
                        Log.d("Bluetoothhandle", "Connection start")
                    }
                    // 接続失敗
                    BluetoothService.STATE_CONNECT_FAILED -> {
                        Log.d("Bluetoothhandle", "Connection Failed")
                    }
                    // 接続完了
                    BluetoothService.STATE_CONNECTED -> {
                        Log.d("Bluetoothhandle", "Connection complete")
                        mBluetoothEnable.value = true
                    }
                    // 接続ロスト
                    BluetoothService.STATE_CONNECTION_LOST -> {
                        Log.d("Bluetoothhandle", "Lost connection")
                        mBluetoothEnable.value = false
                    }
                    // 切断開始
                    BluetoothService.STATE_DISCONNECT_START -> {
                        Log.d("Bluetoothhandle", "Disconnection start")
                    }
                    // 切断完了
                    BluetoothService.STATE_DISCONNECTED -> {
                        Log.d("Bluetoothhandle", "Disconnection complete")
                        mBluetoothEnable.value = false
                    }
                }

                // Bluetoothモジュールからの文字列取得
                BluetoothService.MESSAGE_READ -> {
                    Log.d("Bluetoothhandle", "Message read")
                    var mTempBuffer = ByteArray(BT_BUFFER_SIZE)
                    var i: Int = 0
                    // 読み込んだメッセージを格納
                    mTempBuffer = msg.obj as ByteArray
                    val iCountBuf = msg.arg1;

                    // readしたbyte数をプラスしていく
                    cntTemp += iCountBuf
                    // readされた分Bufferに格納していく
                    for (i in 1..iCountBuf) {
                        mReadTempBuffer[loopCnt]= mTempBuffer[i - 1]
                        loopCnt++
                    }
                    // 一定数溜まったら処理を実行する
                    cntTemp = when {
                        cntTemp >= 10 -> 0
                        cntTemp == 7 -> 0
                        cntTemp == 3 -> 0
                        else -> return
                    }

                    Log.d("Test","mTempBuffer ->${mReadTempBuffer[0]} , ${mReadTempBuffer[1]} , ${mReadTempBuffer[2]} , ${mReadTempBuffer[3]} , ${mReadTempBuffer[4]} , ${mReadTempBuffer[5]} , ${mReadTempBuffer[7]} ,${mReadTempBuffer[8]} , ${mReadTempBuffer[9]} , ${mReadTempBuffer[10]}")

                    // 送られてきたByteが0番目のByteがStartByteなら値を格納していく
                    if(mReadTempBuffer[0] == START_BYTE || mReadTempBuffer[1] == START_BYTE ){
                        loopCnt = 0
                        while (true){
                            // BufferSizeTestを超えている場合終了
                            if(loopCnt >= BT_BUFFER_SIZE - 1){
                                break
                            }
                            // Byteを格納
                            mReadBuffer[i] = mReadTempBuffer[loopCnt]
                            // EndByteが来たらLoopを抜ける
                            if(mReadTempBuffer[loopCnt] == END_BYTE){
                                // LiveDataへ格納
                                when (mReadBuffer[1]) {
                                    // hit
                                    0x01.toByte() -> {
                                        Log.d("Bluetoothhandle", "hit->")
                                        Log.d("Bluetoothhandle","mTempBuffer ->${mReadBuffer[0]} , ${mReadBuffer[1]} , ${mReadBuffer[2]} , ${mReadBuffer[3]} , ${mReadBuffer[4]} , ${mReadBuffer[5]} , ${mReadBuffer[7]} ,${mReadBuffer[8]} , ${mReadBuffer[9]} , ${mReadBuffer[10]}")

                                            hitByteArray.value = mReadBuffer

                                    }
                                    // shoot
                                    0x02.toByte() -> {
                                        Log.d("Bluetoothhandle", "shoot->")
                                        Log.d("Bluetoothhandle","mTempBuffer ->${mReadBuffer[0]} , ${mReadBuffer[1]} , ${mReadBuffer[2]} , ${mReadBuffer[3]} , ${mReadBuffer[4]} , ${mReadBuffer[5]} , ${mReadBuffer[7]} ,${mReadBuffer[8]} , ${mReadBuffer[9]} , ${mReadBuffer[10]}")
                                    }
                                }
                                // END_BYTEの次がSTART_BYTEならまたループを始める
                                if(mReadTempBuffer[loopCnt + 1] == START_BYTE){
                                    // 配列、ループカウントの初期化
                                    for (i in 0 until BT_BUFFER_SIZE) {
                                        mReadBuffer[i]= 0.toByte()
                                    }
                                    i = 0
                                    loopCnt++
                                    continue
                                } else {
                                    // END_BYTEの次がSTART_BYTE以外ならループ終了
                                    break
                                }
                            }
                            // カウントアップ
                            i++
                            loopCnt++
                        }
                    }
                    for (i in 0 until BT_BUFFER_SIZE) {
                        mReadTempBuffer[i]= 0.toByte()
                    }
                    loopCnt = 0
                    Log.d("Bluetoothhandle", "Message read END")
                }

                // Bluetoothモジュールからの文字列送信
                BluetoothService.MESSAGE_WRITE -> {
                    Log.d("Bluetoothhandle", "Message write")
                }
            }
        }
    }

}