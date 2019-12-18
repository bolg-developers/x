package com.example.bolg.standby.host

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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
 * HostStandbyActivity
 * ・部屋生成者の待機画面
 * ・参加ユーザーのリスト表示
 * ・ゲームルールの決定画面のView
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class HostStandbyActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var hostStandbyViewModel: HostStandbyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_standby)

        // root view
        val decorView = window.decorView

        /** widget init **/
        val userId: TextView = findViewById(R.id.host_user_id)
        val readyMember: TextView = findViewById(R.id.host_ready_text)
        val progress: ProgressBar = findViewById(R.id.host_pairing_progress)
        val hostPairing: ImageButton = findViewById(R.id.host_pairing)
        val kakinBullet: Button = findViewById(R.id.host_kakin_bullet)
        val item: Button = findViewById(R.id.host_item_btn)
        val inventory: Button = findViewById(R.id.host_inventory_btn)
        val start: Button = findViewById(R.id.host_ready_btn)
        val ruleSpinner: Spinner = findViewById(R.id.host_game_rule_spinner)
        val joinUser: RecyclerView = findViewById(R.id.host_standby_recycler_view)
        val joinNum: TextView = findViewById(R.id.host_join_text)

        /** SharedPreferences **/
        val data: SharedPreferences = getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)
        userId.text = "${data.getString("token", "error")}"
        Log.d("createAndJoinRoomTask", "token ->" + data.getString("token", "取得出来てない"))

        /** spinner init **/
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            ruleSpinner.adapter = adapter
        }
        ruleSpinner.onItemSelectedListener = this

        /** viewModel init **/
        val application: Application = requireNotNull(this).application
        val viewModelFactoryHost = HostStandbyViewModelFactory(application)
        hostStandbyViewModel =
            ViewModelProviders.of(this, viewModelFactoryHost).get(HostStandbyViewModel::class.java)


        /**************************ViewModelに分割したい*********************************************************/
        // LayoutManagerの設定
        val layoutManager = LinearLayoutManager(this)
        joinUser.layoutManager = layoutManager
        // Adapterの設定
        val sampleList: MutableList<ListData> = mutableListOf()
        for (i: Int in 0..10) { sampleList.add(i, ListData("hasegawa${i}")) }
        val adapter = StandbyRecyclerAdapter(sampleList)
        joinUser.adapter = adapter
        // 区切り線の表示
        joinUser.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        /******************************************************************************************************/

        // ready request
        hostStandbyViewModel.setReady(data.getString("token", "0:0"),decorView)

        /** onClick **/
        // 課金ボタンON/OFF
        kakinBullet.setOnClickListener {
            if (hostStandbyViewModel.kakinBulletState) {
                Log.d("button", "kakinbullet:OFF")
            } else {
                Log.d("button", "kakinbullet:ON")
            }
            hostStandbyViewModel.kakinBulletState = !hostStandbyViewModel.kakinBulletState
        }

        // アイテムボタンON/OFF
        item.setOnClickListener {
            if (hostStandbyViewModel.itemState) {
                Log.d("button", "item:OFF")
            } else {
                Log.d("button", "item:ON")
            }
            hostStandbyViewModel.itemState = !hostStandbyViewModel.itemState
        }

        // ペアリング
        hostPairing.setOnClickListener {
            progress.visibility = ProgressBar.VISIBLE
            Log.d("button", "progress:ON")
            if (hostStandbyViewModel.pairing(decorView)) {
                progress.visibility = ProgressBar.INVISIBLE
                Log.d("button", "progress:OFF")
            }
        }

        // ゲームスタート
        start.setOnClickListener {
            hostStandbyViewModel.startGame(data.getString("token", "0:0"),decorView)
        }

        // インベントリ
        inventory.setOnClickListener { hostStandbyViewModel.inventory(data.getString("token", "0:0").toString()) }

        /** observer **/
        // 準備完了人数の更新
        hostStandbyViewModel.ready.observe(this, Observer { ready ->
            readyMember.text = ready
        })

        // 入室者数
        hostStandbyViewModel.readyPlayerOwner.observe(this, Observer { join ->
            joinNum.text = join
        })
    }

    /** **********************************************************************
     * onItemSelected
     * @param parent
     * @param view
     * @param position
     * @param id
     * 選択ItemをViewに反映させる
     * @author 長谷川　勇太
     * ********************************************************************** */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val spinnerParent: Spinner = parent as Spinner
        val item: String = spinnerParent.selectedItem as String
        hostStandbyViewModel.updateGameRule(item)
    }

    /** **********************************************************************
     * onNothingSelected
     * @param parent
     * @author 長谷川　勇太
     * ********************************************************************** */
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    /** **********************************************************************
     * onStart
     * ・Bluetoothデバイスに接続
     * @author 中田　桂介
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
     * @author 中田　桂介
     * ********************************************************************** */
    override fun onResume() {
        super.onResume()
        Log.d("HostStandbyActivity", "onResume")
        BluetoothFunction.getInstance().connect()
    }
    /** **********************************************************************
     * onPause
     * ・Bluetoothデバイスを切断
     * @author 中田　桂介
     * ********************************************************************** */
    public override fun onPause() {
        super.onPause()  // Always call the superclass method first
        Log.d("HostStandbyActivity", "onPause")
        /*if (null != BluetoothFunction.getInstance().mBluetoothService) {
            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
            BluetoothFunction.getInstance().mBluetoothService = null
        }*/
    }
    /** **********************************************************************
     * onStop
     * ・Bluetoothデバイスを切断
     * @author 中田　桂介
     * ********************************************************************** */
    public override fun onStop() {
        super.onStop()
        Log.d("HostStandbyActivity", "onStop")
        /*if (null != BluetoothFunction.getInstance().mBluetoothService) {
            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
            BluetoothFunction.getInstance().mBluetoothService = null
        }*/
    }
    /** **********************************************************************
     * onDestroy
     * ・Bluetoothデバイスを切断
     * @author 中田　桂介
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