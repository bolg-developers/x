package com.example.bolg.standby.player

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.GrpcTask
import com.example.bolg.adapter.StandbyRecyclerAdapter
import kotlinx.coroutines.*
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.data.ListData

/** ----------------------------------------------------------------------
 * PlayerStandbyViewModel
 * @param application : AndroidViewModelの引数に使用
 * ・HOSTが変更したゲームルールをViewに反映させる
 * ・インベントリの設定
 * ・上記完了後、準備完了を押下し待機
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class PlayerStandbyViewModel(application: Application): AndroidViewModel(application){

    /** Coroutine定義 **/
    // Job Set
    private var viewModelJob = Job()
    // Scope Set
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var layoutManager: LinearLayoutManager? = null
    private val sampleList: MutableList<ListData> = mutableListOf()

    private val app: Application = application
    val data: SharedPreferences = app.getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)

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

    /** **********************************************************************
     * setReady
     * @param token プレイヤー情報
     * @param view View
     * @author 長谷川　勇太
     * ********************************************************************** */
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
    fun pairing(view: View?): Boolean{
        BluetoothFunction.getInstance().btPairing()
        return true
    }

    /** **********************************************************************
     * updateWeapon
     * @param attack 攻撃力
     * @param token　トークン
     * @param view   View
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun updateWeapon(attack: Long, token: String?, view: View?) {
        uiScope.launch {
            GrpcTask.getInstance(app).updateWeapon(attack,token,view)
        }
    }

    /** **********************************************************************
     * updateList
     * @param context Context
     * @param joinUser
     * @param name
     * @author 長谷川　勇太
     * ********************************************************************** */
    fun updateList(context: Context, joinUser: RecyclerView, name: String){
        // LayoutManagerの設定
        layoutManager = LinearLayoutManager(context)
        joinUser.layoutManager = layoutManager
        // Adapterの設定
        sampleList.add(ListData(name))
        val adapter = StandbyRecyclerAdapter(sampleList)
        joinUser.adapter = adapter
        // 区切り線の表示
        joinUser.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}