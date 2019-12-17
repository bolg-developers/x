package com.example.bolg.standby.player

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bolg.GrpcTask
import kotlinx.coroutines.*
import com.example.bolg.bluetooth.BluetoothFunction

/** ----------------------------------------------------------------------
 * PlayerStandbyViewModel
 * @param application : AndroidViewModelの引数に使用
 * ・HOSTが変更したゲームルールをViewに反映させる
 * ・インベントリの設定
 * ・上記完了後、準備完了を押下し待機
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class PlayerStandbyViewModel(application: Application): AndroidViewModel(application){

    // Jobの定義
    private var viewModelJob = Job()
    // スコープの定義
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val app: Application = application

    /** LiveDataの設定 **/
    private val _gameState = MutableLiveData<Boolean>()
    val gameState: LiveData<Boolean>
        get() = _gameState

    private val _gameRule = MutableLiveData<String>()
    val gameRule: LiveData<String>
        get() = _gameRule

    //var gameState = true

    // 準備完了人数
    private val _readyPlayerNormal = MutableLiveData<String>()
    val readyPlayerNormal: LiveData<String>
        get() = _readyPlayerNormal

    // 課金ボタンON/OFF
    private val _kakinBulletState = MutableLiveData<Boolean>()
    val kakinBulletState: LiveData<Boolean>
        get() = _kakinBulletState

    private val _itemState = MutableLiveData<Boolean>()
    val itemState: LiveData<Boolean>
        get() = _itemState

    /**
     * HOSTが設定した値を元に変更
     * */
    fun setReady(token: String, view: View?){
        uiScope.launch {
            GrpcTask.getInstance(app).setReady(token, view)
        }
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
}