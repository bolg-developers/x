package com.example.bolg.gameplay

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.bolg.BoLG_Audio
import com.example.bolg.GrpcTask
import com.example.bolg.R
import com.example.bolg.adapter.StandbyRecyclerAdapter
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.data.ListData
import com.example.bolg.gameplay.GamePlayViewModel.Companion.LOSER
import com.example.bolg.gameplay.GamePlayViewModel.Companion.WINNER
import kotlinx.android.synthetic.main.activity_game_play.*
import java.nio.ByteBuffer

/** ----------------------------------------------------------------------
 * GamePlayActivity
 * ゲームプレイ中画面
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
@Suppress("NAME_SHADOWING", "UNREACHABLE_CODE", "DEPRECATION")
class GamePlayActivity : AppCompatActivity(){

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)

        BluetoothFunction.getInstance().connect()

        var hitCnt = 0
        var hitNameFlg = false

        // root view
        val decorView: View = window.decorView
        // hide navigation bar, hide status bar
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
        val mVibrator =  getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val application: Application = requireNotNull(this).application
        val viewModelFactory = GamePlayViewModelFactory(application)
        val gamePlayViewModel:GamePlayViewModel =
            ViewModelProviders.of(this,viewModelFactory)
                .get(GamePlayViewModel::class.java)

        /** widget init **/
        val playerHp   : TextView     = findViewById(R.id.hp)
        val joinUser   : RecyclerView = findViewById(R.id.list)
        val hpRecovery : ImageButton  = findViewById(R.id.test_hp_recovery_btn)
        val item1      : ImageView    = findViewById(R.id.item1)

        /** SharedPreferences **/
        val data: SharedPreferences =
            getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = data.edit()

        // gameEnd init
        editor?.putBoolean("end_game",false)
        editor?.apply()

        // HP 初期値
        playerHp.text = 100.toString()

        /** Toolbar init **/
        game_toolbar.title =
            data.getString("player_name","player_name_none") +
            data.getString("token","player_name_none")

        // playerIdをByteArrayに変換する
        val playerId = data.getLong("player_id",99).toInt()
        Log.d("GamePlayActivity", playerId.toString())

        val bytes = ByteBuffer.allocate(4).putInt(playerId).array()
        Log.d("GamePlayActivity",
            "mTempBuffer ->" +
                    "${bytes[0]} , " +
                    "${bytes[1]} , " +
                    "${bytes[2]} , " +
                    "${bytes[3]} ")

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

        Log.d("GamePlayActivity",
            "SENDBuffer ->" +
                    "${integers[0]} , " +
                    "${integers[1]} , " +
                    "${integers[2]} , " +
                    "${integers[3]}, " +
                    "${integers[4]} , " +
                    "${integers[5]} , " +
                    "${integers[6]} ")

        if(!BluetoothFunction.getInstance().write(integers)){
            Log.d("createAndJoinRoomTask","writeError")
        }

        // Observe : 弾を撃った時に動く
        BluetoothFunction
            .getInstance()
            .shootByteArray
            .observe(this , Observer { readByte ->
                Log.d("GamePlayActivity" , "Bluetooth read ByteArray")
                // Bluetoothの値GamePlayViewModelへ送る
                // ショット音を鳴らす
                BoLG_Audio.getInstance().play(BoLG_Audio.AudioID.SHOT,0,1.0f)

                gamePlayViewModel.btShootRead(readByte)
        })

        // Observe : 弾を被弾時に動く
        BluetoothFunction
            .getInstance()
            .hitByteArray
            .observe(this , Observer { readByte ->
            //  一回目はスルーする
            if(hitCnt != 0) {
                // 被弾音を鳴らす
                BoLG_Audio.getInstance().play(BoLG_Audio.AudioID.HIT,0,1.0f)

                Log.d("GamePlayActivityHit", "Bluetooth read ByteArray")
                // Bluetoothの値GamePlayViewModelへ送る
                gamePlayViewModel.btHitRead(readByte, decorView)
            }
        })

        // Bluetooth接続の可否
        BluetoothFunction.getInstance().mBluetoothEnable.observe(this, Observer { BluetoothEnable ->
            if (BluetoothEnable){
                Log.d("BluetoothEnable_true", "observe")
                item1.setImageResource(R.drawable.bolg_bluetooth_enable_light)
            } else {
                Log.d("BluetoothEnable_false", "observe")
                item1.setImageResource(R.drawable.bolg_bluetooth_enable_dark)
            }
        })

        /** Observe kind **/
        // HP更新
        GrpcTask
            .getInstance(application)
            .hitFlg
            .observe(this, Observer { hp->
            Log.d("GamePlayActivity" , "HP Update")
            if(hitCnt > 0) {
                mVibrator.vibrate(100)
                playerHp.text = hp.toString()
            }
            hitCnt++
        })

        GrpcTask
            .getInstance(application)
            .gameEndFlg
            .observe(this, Observer {result->
            Log.d("GamePlayActivity" , "game end")
            val winnerName = result.survivalResultMsg.winner.name
            var winOfLose = WINNER
            var winOfLoseImg = R.drawable.ic_thumb_up_black_24dp

            if(winnerName != data.getString("player_name","Error") ){
                winOfLose = LOSER
                winOfLoseImg = R.drawable.ic_thumb_down_black_24dp
            }

            if(data.getBoolean("end_game",false)) {

                val resultString =
                    result
                        .survivalResultMsg
                        .personalsOrBuilderList[0]
                        .playerName + " -> \n" + "KILL :  " +
                            "${result.survivalResultMsg.personalsOrBuilderList[0].killCount}回,　　　　　" +
                            result
                                .survivalResultMsg
                                .personalsOrBuilderList[1]
                                .playerName + " -> \n" + "KILL :  " +
                            "${result.survivalResultMsg.personalsOrBuilderList[1].killCount}回"

                val dialog = SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText(winOfLose)
                    .setCustomImage(winOfLoseImg)
                    .setContentText(resultString)
                    .setConfirmText("←")
                    .setConfirmClickListener {
                        Log.d("GAME","←")
                        editor?.putBoolean("loop_state", true)
                        editor?.putBoolean("retry_state", false)
                        editor?.commit()
                        finish()
                    }
                    .setCancelText("×")
                    .setCancelClickListener {
                        Log.d("GAME","×")
                        editor?.putBoolean("loop_state", true)
                        editor?.putBoolean("retry_state", true)
                        editor?.commit()
                        finish()
                    }
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()

            }
        })

        // 被弾者通知
        GrpcTask
            .getInstance(application)
            .hitName.observe(this, Observer { name->
            editor?.putBoolean("end_game",true)
            editor?.apply()
            if(hitNameFlg) {
                log.text = name + "が撃たれました！"
            }
            hitNameFlg = true
        })

        GrpcTask
            .getInstance(application)
            .gameUserNameList
            .observe(this, Observer { joinUserList->
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

        /** onClick processing **/
        // HP（１００）からの現状のHPの回復
        hpRecovery.setOnClickListener {
            val token: String? = data.getString("token", "0:0")
            gamePlayViewModel.recovery(100L,token,decorView)
            Toast.makeText(applicationContext, "回復しました。", Toast.LENGTH_LONG).show()
        }

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