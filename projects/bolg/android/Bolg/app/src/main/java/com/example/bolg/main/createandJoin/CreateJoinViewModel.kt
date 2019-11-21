package com.example.bolg.main.createandJoin

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import com.example.bolg.R
import com.example.bolg.standby.host.HostStandbyActivity
import com.example.bolg.standby.player.PlayerStandbyActivity


/** ----------------------------------------------------------------------
 * CreateJoinViewModel
 * @param application : AndroidViewModelの引数に使用
 * ・参加ボタン押下時の部屋ID入力Dialog表示
 * ・ホスト待機画面遷移
 * ・プレイヤー待機画面遷移
 * ---------------------------------------------------------------------- */
class CreateJoinViewModel (application: Application): AndroidViewModel(application){

    private lateinit var intent: Intent

    var context: Context? = null

    /** **********************************************************************
     * joinDialog
     * @param view
     * 生成されている部屋に入室のため、部屋IDを入力する
     * ********************************************************************** */
    fun joinDialog(view: View){
        Log.d("createjoin","onjoinDialog")

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
                Log.d("createjoin","onjoinDialog/CancelButton")
                //キャンセルボタンを押したときの処理
            })
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, whichButton ->
                //ＯＫボタンを押したときの処理
                // 入力されたルームIDが存在するか
                Log.d("createjoin","onjoinDialog/OKButton")
                if(roomIdCheck(editText.text.toString())){
                    join(view)
                }
            })
            .show()
    }

    /** **********************************************************************
     * join
     * @param view 親のview
     * PlayerStandbyActivity(参加者待機画面)への遷移
     * ********************************************************************** */
    private fun join(view: View){
        Log.d("createjoin","onjoin")
        context = view.context
        intent = Intent(view.context, PlayerStandbyActivity::class.java)
        context?.startActivity(intent)
    }

    /** **********************************************************************
     * create
     * @param view 親のview
     * HostStandbyActivity(ホスト待機画面)への遷移
     * ********************************************************************** */
    fun create(view: View){
        Log.d("createjoin","oncreate")
        context = view.context
        intent = Intent(context, HostStandbyActivity::class.java)
        context?.startActivity(intent)
    }

    private fun roomIdCheck(roomid: String): Boolean{

        Log.d("room",roomid.toString())

        return true
    }
}