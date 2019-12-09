package com.example.bolg.standby.player

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.adapter.StandbyRecyclerAdapter
import com.example.bolg.R
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.data.ListData

/** ----------------------------------------------------------------------
 * クラス名 PlayerStandbyActivity
 * ・概要1
 * ・概要2
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class PlayerStandbyActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_standby)

        /** widget init **/
        val progress  : ProgressBar =  findViewById(R.id.progress)
        val playerPairing : ImageButton = findViewById(R.id.player_pairing)
        val joinuser  : RecyclerView = findViewById(R.id.standby_recycler_view)
        val ready     : Button = findViewById(R.id.player_ready_btn)
        val rule      : TextView = findViewById(R.id.rule)

        /** viewModel類 **/
        val application: Application = requireNotNull(this).application
        val viewModelFactoryplayer: PlayerStandbyViewModelFactory = PlayerStandbyViewModelFactory(application)
        val playerStandbyViewModel = ViewModelProviders.of(this,viewModelFactoryplayer).get(PlayerStandbyViewModel::class.java)

        /** RecyclerView init **/
        val layoutManager = LinearLayoutManager(this)
        joinuser.layoutManager = layoutManager
        // Adapterの設定
        var sampleList = mutableListOf<ListData>()
        for (i in 0..10) {
            sampleList.add(i, ListData("hasegawa${i}"))
        }
        val adapter = StandbyRecyclerAdapter(sampleList)
        joinuser.adapter = adapter
        // 区切り線の表示
        joinuser.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


        /** Observe類 **/
        // ゲームルール
        playerStandbyViewModel.gameRule.observe(this, Observer { mrule->
            rule.text = mrule
        })

        // アイテムON/OFF
        playerStandbyViewModel.itemState.observe(this, Observer { item->
            if(item){
                // 青色に変化（ONっぽい表示）
            }else{

            }
        })

        // 課金弾ON/OFF
        playerStandbyViewModel.kakinBulletState.observe(this, Observer { kakinbullet->
            if(kakinbullet){
                // 青色に変化（ONっぽい表示）
            }else{

            }
        })

        /** onClick **/
        ready.setOnClickListener { progress.visibility = ProgressBar.VISIBLE }

        // ペアリング
        playerPairing.setOnClickListener {
            progress.visibility = ProgressBar.VISIBLE
            Log.d("button", "progress:ON")
            if(playerStandbyViewModel.pairing()){
                progress.visibility = ProgressBar.INVISIBLE
                Log.d("button", "progress:OFF")
            }
        }


    }

    /** **********************************************************************
     * onStart
     * ・Bluetoothデバイスに接続
     * ********************************************************************** */
    public override fun onStart() {
        super.onStart()
        Log.d("HostStandbyActivity", "onStart")
        BluetoothFunction.getInstance().connect()
    }

    /** **********************************************************************
     * onRestart
     * ・Bluetoothデバイスに接続
     * ********************************************************************** */
    public override fun onRestart() {
        super.onRestart()
        Log.d("HostStandbyActivity", "onRestart")
        BluetoothFunction.getInstance().connect()
    }

    /** **********************************************************************
     * onResume
     * ・Bluetoothデバイスに接続
     * ********************************************************************** */
    override fun onResume() {
        super.onResume()
        Log.d("HostStandbyActivity", "onResume")
        BluetoothFunction.getInstance().connect()
    }


    /** **********************************************************************
     * onPause
     * ・Bluetoothデバイスを切断
     * ********************************************************************** */
    public override fun onPause() {
        super.onPause()  // Always call the superclass method first
        Log.d("HostStandbyActivity", "onPause")
        if (null != BluetoothFunction.getInstance().mBluetoothService) {
            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
            BluetoothFunction.getInstance().mBluetoothService = null
        }
    }

    /** **********************************************************************
     * onStop
     * ・Bluetoothデバイスを切断
     * ********************************************************************** */
    public override fun onStop() {
        super.onStop()
        Log.d("HostStandbyActivity", "onStop")
        if (null != BluetoothFunction.getInstance().mBluetoothService) {
            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
            BluetoothFunction.getInstance().mBluetoothService = null
        }
    }

    /** **********************************************************************
     * onDestroy
     * ・Bluetoothデバイスを切断
     * ********************************************************************** */
    override fun onDestroy() {
        super.onDestroy()
        Log.d("HostStandbyActivity", "onDestroy")
        if (null != BluetoothFunction.getInstance().mBluetoothService) {
            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
            BluetoothFunction.getInstance().mBluetoothService = null
        }
    }
}