package com.example.bolg.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import android.content.DialogInterface
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.bolg.R
import com.example.bolg.standby.HostStandbyActivity
import com.example.bolg.standby.PlayerStandbyActivity


class CreateJoinFragment : Fragment(){
    private var intent: Intent?  = null

    // Fragmentが初めてUIを描画する時にシステムが呼び出す
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_main,container,false)

        val joinBtn = view.findViewById<Button>(R.id.join_btn)
        val createBtn = view.findViewById<Button>(R.id.create_btn)

        // 参加
        joinBtn.setOnClickListener {
            // Dialog表示
            joinDialog(view)
        }

        // 部屋生成
        createBtn.setOnClickListener {
            intent = Intent(context, HostStandbyActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    /**
     * joinDialog
     * @param view
     * 生成されている部屋に入室のため、部屋IDを入力する
     * */
    private fun joinDialog(view:View){

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
                //キャンセルボタンを押したときの処理
            })
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, whichButton ->
                //ＯＫボタンを押したときの処理
                // 入力されたルームIDが存在するか
                intent = Intent(context, PlayerStandbyActivity::class.java)
                startActivity(intent)
            })
            .show()
    }
}