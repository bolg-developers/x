package com.example.bolg.main.createandJoin

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import com.example.bolg.GrpcTask
import com.example.bolg.R
import com.example.bolg.standby.host.HostStandbyActivity
import com.example.bolg.standby.player.PlayerStandbyActivity
import kotlinx.coroutines.*

/** ----------------------------------------------------------------------
 * CreateJoinViewModel
 * @param application : AndroidViewModelの引数に使用
 * ・参加ボタン押下時の部屋ID入力Dialog表示
 * ・ホスト待機画面遷移
 * ・プレイヤー待機画面遷移
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class CreateJoinViewModel (application: Application): AndroidViewModel(application){

    private lateinit var intent: Intent

    //  SharedPreferenceのインスタンス生成
    val data: SharedPreferences = application.getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor? = data.edit()

    // Jobの定義
    private var viewModelJob = Job()

    // スコープの定義
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val grpcTask = GrpcTask(application)

    /** **********************************************************************
     * joinDialog
     * @param view
     * 生成されている部屋に入室のため、部屋IDを入力する
     * ********************************************************************** */
    fun joinDialog(view: View){
        Log.d("createJoin","onJoinDialog")

        // Input widget
        val editText = EditText(view.context)
        editText.hint = "ルームID"

        // Dialog設定/表示
        AlertDialog.Builder(view.context)
            .setCancelable(false)
            .setIcon(R.mipmap.ic_launcher)
            .setTitle("ルームID入力")
            .setMessage("ルームIDを入力してください。\n（数字4桁）")
            .setView(editText)
            .setNegativeButton("キャンセル", DialogInterface.OnClickListener { dialog, whichButton ->
            })
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, whichButton ->
                // 空白でないか
                if(editText.text.isEmpty()){
                    Toast.makeText(view.context, "入力が空白です", Toast.LENGTH_LONG).show()
                }
                else if(editText.text.length < 4 || editText.text.length > 5){
                    Toast.makeText(view.context, "部屋のIDは4桁です", Toast.LENGTH_LONG).show()
                }
                // 部屋への参加Request
                else{
                    uiScope.launch {
                            Log.d("createJoin","JoinRequest")
                            grpcTask.joinRoomTask(editText.text.toString().toLong(),view)
                    }
                }
            })
            .show()
    }

    /** **********************************************************************
     * create
     * @param view 親のview
     * HostStandbyActivity(ホスト待機画面)への遷移
     * ********************************************************************** */
    fun create(view: View){
        Log.d("createAndJoinRoomTask","fun create start")
        uiScope.launch {
<<<<<<< HEAD
            async(Dispatchers.Default) {
                grpcTask.createAndJoinRoomTask("yuta")
            }.await().let {

                if(it.createAndJoinRoomResp.room.id == 0L) {
                    // Data Put (player_id/player_id/player_readyは固定でテスト)
                    editor?.putLong("room_id", it.createAndJoinRoomResp.room.id)
                    editor?.putLong("player_id", 1111)
                    editor?.putLong("player_hp", 100)
                    editor?.putBoolean("player_ready", true)
                    editor?.putLong("owner_id", it.createAndJoinRoomResp.room.ownerId)
                    editor?.putInt("game_rule", it.createAndJoinRoomResp.room.gameRule.number)
                    editor?.putBoolean("game_start", it.createAndJoinRoomResp.room.gameStart)
                    editor?.putString("player_name", "yuta")
                    editor?.putString("token", it.createAndJoinRoomResp?.token)
                    editor?.apply()

                    context = view.context
                    intent = Intent(context, HostStandbyActivity::class.java)
                    context?.startActivity(intent)
                }

            }
=======
            grpcTask.createAndJoinRoomTask(view)
>>>>>>> f23809494af8ae96691beb405917d6e3f3e27f44
        }
        Log.d("createAndJoinRoomTask","fun create end")
    }

    // 通信確認()
//    fun test(){
//        uiScope.launch {
//            grpcTask.test()
//        }
//    }

}