package com.example.bolg.standby.host

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.gameplay.GamePlayActivity

/** ----------------------------------------------------------------------
 * HostStandbyViewModel
 * @param application : AndroidViewModelの引数に使用
 * ・インベントリの設定（まだ実装予定なし）
 * ・ペアリング (11/21までに実装予定)
 * ・準備完了通知　(未実装)
 * ・HOSTが設定したゲームルールえをリアルタイムで表示 (未実装)
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class HostStandbyViewModel (application: Application): AndroidViewModel(application){

    // intent vars
    private lateinit var intent: Intent
    private var context: Context? = null

    // GameRule
    private val _gameRule = MutableLiveData<String>()
    val gameRule: LiveData<String>
        get() = _gameRule

    //var gameState = true

    // 課金ボタンON/OFF
    var kakinBulletState = true

    var itemState = true

//    fun setList(){
//    }

//    fun setState(state: Boolean){
//        gameState = state
//    }

    /** **********************************************************************
     * startGame
     * @param mcontext Activityのcontext
     * ********************************************************************** */
    fun startGame(mcontext: Context){
        context = mcontext
        intent = Intent(context, GamePlayActivity::class.java)
        context?.startActivity(intent)
    }

    /** **********************************************************************
     * updateGameRule
     * @param rule 選択されたゲームルール
     * ********************************************************************** */
    fun updateGameRule(rule: String){
        _gameRule.value = rule
        Log.d("選択されたルール",rule)
    }

    /** **********************************************************************
     * pairing
     * @return 成功/失敗
     * ********************************************************************** */
    fun pairing(): Boolean{
        BluetoothFunction.getInstance().btPairing()
        return true
    }

}