package com.example.bolg.standby.host

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bolg.GrpcTask
import kotlinx.coroutines.*
import com.example.bolg.bluetooth.BluetoothFunction

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

    // Jobの定義
    private var viewModelJob = Job()
    // スコープの定義
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val app = application

    // GameRule
    private val _gameRule = MutableLiveData<String>()
    val gameRule: LiveData<String>
        get() = _gameRule

    // 準備完了人数
    private val _readyPlayerOwner = MutableLiveData<String>()
    val readyPlayerOwner: LiveData<String>
        get() = _readyPlayerOwner

    // 課金ボタンON/OFF
    var kakinBulletState = true
    var itemState = true

    // 準備完了人数の更新
    private val _ready = MutableLiveData<String>()
    val ready: LiveData<String>
        get() = _ready

    /** **********************************************************************
     * startGame
     * @param token プレイヤー情報
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun startGame(token: String?, view: View?) {
        uiScope.launch {
            GrpcTask.getInstance(app).setReady(token,view)
            delay(100)
            GrpcTask.getInstance(app).startGame(token,view)
        }
    }

    /** **********************************************************************
     * updateGameRule
     * @param rule 選択されたゲームルール
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun updateGameRule(rule: String) {
        _gameRule.value = rule
        Log.d("選択されたルール", rule)
    }

    /** **********************************************************************
     * pairing
     * @return 成功/失敗
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun pairing(): Boolean{
        BluetoothFunction.getInstance().btPairing()
        return true
    }

    /** **********************************************************************
     * updateWeapon
     * @param attack 攻撃力
     * @param token　トークン
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun updateWeapon(attack: Long, token: String?, view: View?) {
        uiScope.launch {
            GrpcTask.getInstance(app).updateWeapon(attack,token,view)
        }
    }

    /** **********************************************************************
     * inventory
     * @param token トークン
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun inventory(token: String) {
        uiScope.launch {
            GrpcTask.getInstance(app).inventory(token)
        }
    }
}