package com.example.bolg.main.createandJoin

import android.app.Application
import android.content.DialogInterface
import android.content.Intent
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
    private val app: Application = application
    // Jobの定義
    private var viewModelJob = Job()
    // スコープの定義
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /** **********************************************************************
     * joinDialog
     * @param view
     * 生成されている部屋に入室のため、部屋IDを入力する
     * @author 長谷川　勇太
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
                        Log.d("GrpcTask","JoinRequest")
                        GrpcTask.getInstance(app).joinRoomTask(editText.text.toString().toLong(), view)
                    }
                    Log.d("GrpcTask","joinRoomTaskEnd")
                }
            })
            .show() 
    }

    /** **********************************************************************
     * create
     * HostStandbyActivity(ホスト待機画面)への遷移
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun create(view: View){
        Log.d("createAndJoinRoomTask","fun create start")
        uiScope.launch {
            GrpcTask.getInstance(app).createAndJoinRoomTask(view)
        }
        // HostStandby Transition
        Log.d("GrpcTask","create_and_join_room_respIntent")
    }
}
