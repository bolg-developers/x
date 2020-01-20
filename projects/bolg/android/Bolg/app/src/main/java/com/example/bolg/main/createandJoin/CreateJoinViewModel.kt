package com.example.bolg.main.createandJoin

import android.app.Application
import android.content.Context
import android.os.Vibrator
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.bolg.GrpcTask
import kotlinx.coroutines.*

/** ----------------------------------------------------------------------
 * CreateJoinViewModel
 * @param application : AndroidViewModelの引数に使用
 * ・参加ボタン押下時の部屋ID入力Dialog表示
 * ・ホスト待機画面遷移
 * ・プレイヤー待機画面遷移
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
@Suppress("DEPRECATION")
class CreateJoinViewModel (application: Application): AndroidViewModel(application){
    private val app: Application = application
    private val mVibrator =  app.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
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
        val joinEdit = EditText(view.context)
        joinEdit.inputType = InputType.TYPE_CLASS_NUMBER
        joinEdit.hint = "ルームID"

        SweetAlertDialog(view.context, SweetAlertDialog.NORMAL_TYPE)
            .setTitleText("ルームID入力")
            .setCustomView(joinEdit)
            .setCancelText("×")
            .setCancelClickListener {
                it.setCancelClickListener(null)
            }
            .setContentText("Join")
            .setConfirmClickListener { sDialog ->
                if(joinEdit.text.isEmpty()){
                    Toast.makeText(view.context, "入力が空白です", Toast.LENGTH_LONG).show()
                    mVibrator.vibrate(50)
                    sDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
                }
                // 部屋への参加Request
                else{
                    uiScope.launch {
                        Log.d("GrpcTask","JoinRequest")
                        GrpcTask.getInstance(app).joinRoomTask(joinEdit.text.toString().toLong(), view)
                        sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                        delay(800)
                        sDialog.cancel()
                    }
                    Log.d("GrpcTask","joinRoomTaskEnd")
                }
            }
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
    }
}
