package com.example.bolg.standby.host

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.GrpcTask
import com.example.bolg.R
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.main.MainActivity
import java.util.*
import java.util.concurrent.TimeUnit
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.bolg.adapter.StandbyRecyclerAdapter
import com.example.bolg.data.ListData
import com.example.bolg.gameplay.GamePlayActivity
import kotlinx.android.synthetic.main.activity_game_play.*
import kotlinx.android.synthetic.main.activity_host_standby.*

/** ----------------------------------------------------------------------
 * HostStandbyActivity
 * ・部屋生成者の待機画面
 * ・参加ユーザーのリスト表示
 * ・ゲームルールの決定画面のView
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
@Suppress("UNREACHABLE_CODE")
class HostStandbyActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var hostStandbyViewModel: HostStandbyViewModel
    /** stamina  info init **/
    private lateinit var timer: CountDownTimer
    private var stamina1: MenuItem? = null
    private var stamina2: MenuItem? = null
    private var stamina3: MenuItem ? = null
    private var listFlg = false
    private var gameStart = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_standby)

        // root view
        val decorView: View= window.decorView
        // hide navigation bar, hide status bar
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE

        /** widget init **/
        val userId: TextView = findViewById(R.id.host_user_id)
        val joinMember: TextView = findViewById(R.id.host_join_num)
        val readyNum: TextView = findViewById(R.id.host_ready_txt)
        val hostBluetoothEnable: ImageView = findViewById(R.id.host_bluetooth_enable)
        val hostPairing: ImageButton = findViewById(R.id.host_pairing)
        val billingAmmunition: ImageButton = findViewById(R.id.host_billing_ammunition_btn)
        val start: ImageButton = findViewById(R.id.host_start_btn)
        val item: ImageButton = findViewById(R.id.host_item_btn)
        val progress: ProgressBar = findViewById(R.id.host_pairing_progress)
        val ruleSpinner: Spinner = findViewById(R.id.host_game_rule_spinner)
        val joinUser: RecyclerView = findViewById(R.id.host_standby_recycler_view)

        /** viewModel init **/
        val application: Application = requireNotNull(this).application
        val viewModelFactoryHost = HostStandbyViewModelFactory(application)
        hostStandbyViewModel =
            ViewModelProviders.of(this, viewModelFactoryHost).get(HostStandbyViewModel::class.java)

        /** SharedPreferences **/
        val data: SharedPreferences =
            getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)
        // write editor get
        val editor: SharedPreferences.Editor? = data.edit()

        // 武器のセット
        hostStandbyViewModel
            .updateWeapon(
                1L, // 攻撃力
                data.getString("token", "0:0"),
                decorView)

        // User ID view
        userId.text = "${data.getString("player_name", "error")}"
        // List Update
        readyNum.text = data.getLong("player_ready_num", 99L).toString()

        /** Toolbar init **/
        setSupportActionBar(host_toolbar)
        host_toolbar.title = data.getString("player_name", "")
        host_toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        host_toolbar.setNavigationOnClickListener {
            listFlg = false
            if (null != BluetoothFunction.getInstance().mBluetoothService) {
                BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
                BluetoothFunction.getInstance().mBluetoothService = null
            }
            finish()
        }

        /** spinner init **/
        val adapter = ArrayAdapter(
            this,
            R.layout.custom_spinner,
            resources.getStringArray(R.array.planets_array)
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        ruleSpinner.adapter = adapter

        /** list init **/
        hostStandbyViewModel.updateList(
            applicationContext,
            joinUser,
            data.getString("player_name", "")!!,
            1
        )

        /** CountDownTimer init **/
        timer = object : CountDownTimer(data.getLong("nowTimer", 0), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // "00:00:00"の方式で表示する。
                host_toolbar.title = String.format(
                    Locale.getDefault(), "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                )
            }

            override fun onFinish() {
                host_toolbar.title = data.getString("player_name", "")
                stamina1?.setIcon(R.drawable.bolg_stamina_on)
                editor?.putBoolean("staminaFirst", true)
                editor?.putBoolean("staminaSecond", true)
                editor?.putBoolean("staminaThird", true)
                editor?.apply()
            }
        }.start()

        /** onClick processing **/
        // 課金ボタンON/OFF
        billingAmmunition.setOnClickListener {
            if (hostStandbyViewModel.kakinBulletState) {
                Log.d("button", "課金ボタン:OFF")
            } else {
                Log.d("button", "課金ボタン:ON")
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
            if(!data.getBoolean("loop_state",false)) {
                progress.visibility = ProgressBar.VISIBLE
                Log.d("button", "progress:ON")
                if (hostStandbyViewModel.pairing(decorView)) {
                    progress.visibility = ProgressBar.INVISIBLE
                    // ready request
                    hostStandbyViewModel.setReady(data.getString("token", "0:0"), decorView)
                    //hostPairing.isEnabled = false
                }
            }
            else{
                BluetoothFunction.getInstance().connect()
                // ready request
                hostStandbyViewModel.setReady(data.getString("token", "0:0"), decorView)
                //hostPairing.isEnabled = false
            }
        }

        // ゲームスタート
        start.setOnClickListener {
            hostStandbyViewModel.startGame(data.getString("token", "0:0"), decorView)
        }

        /** observer kind **/
        // 入室者数処理
        GrpcTask.getInstance(application).joinUserNum.observe(this, Observer { joinNum ->
            Log.d("Host", "${joinNum}人が参加しています")
            joinMember.text = joinNum.toString()
        })

        // 準備完了処理
        GrpcTask.getInstance(application).readyFlg.observe(this, Observer {
            readyNum.text = data.getLong("player_ready_num", 99L).toString()
            Log.d("player_ready_num", "observe")
        })

        // 入室リスト更新
        GrpcTask.getInstance(application).userNameList.observe(this, Observer { joinUserList ->
            // List Update
            Log.d("RecyclerList", "observe: joinUserList -> $joinUserList")
            if (listFlg) {

                val sampleList: MutableList<ListData> = mutableListOf()

                for (i in 0 until joinUserList.size) {
                    sampleList.add(ListData(joinUserList[i]))
                }
                val mAdapter = StandbyRecyclerAdapter(sampleList)
                joinUser.adapter = mAdapter

                // 区切り線の表示
                joinUser.addItemDecoration(
                    DividerItemDecoration(
                        applicationContext,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
            listFlg = true
        })

        // ゲーム開始
        GrpcTask.getInstance(application).gameStart.observe(this, Observer {
            if (gameStart) {
                val intent = Intent(applicationContext, GamePlayActivity::class.java)
                startActivity(intent)
            }
            gameStart = true
        })

        // Bluetooth接続の可否
        BluetoothFunction.getInstance().mBluetoothEnable.observe(this, Observer { BluetoothEnable ->
            if (BluetoothEnable){
                Log.d("BluetoothEnable_true", "observe")
                start.isEnabled = true
                start.setImageResource(R.drawable.bolg_start_on_right)
                hostBluetoothEnable.setImageResource(R.drawable.bolg_bluetooth_enable_light)
            } else {
                Log.d("BluetoothEnable_false", "observe")
                start.isEnabled = false
                start.setImageResource(R.drawable.bolg_start_on_dark)
                hostBluetoothEnable.setImageResource(R.drawable.bolg_bluetooth_enable_dark)
            }
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
//        BluetoothFunction.getInstance().connect()
    }
    /** **********************************************************************
     * onRestart
     * ・Bluetoothデバイスに接続
     * ********************************************************************** */
    public override fun onRestart() {
        super.onRestart()
        Log.d("HostStandbyActivity", "onRestart")
        val data: SharedPreferences = getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)

        if(data.getBoolean("retry_state",false)){
            finish()
        }

        if (data.getBoolean("loop_state", false)) {
            if (null != BluetoothFunction.getInstance().mBluetoothService) {
                BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
                BluetoothFunction.getInstance().mBluetoothService = null
            }

            host_start_btn.isEnabled = false
            host_pairing.isEnabled = true
            host_start_btn.setImageResource(R.drawable.bolg_start_on_dark)
        }
    }
    /** **********************************************************************
     * onResume
     * ・Bluetoothデバイスに接続
     * @author 中田　桂介
     * ********************************************************************** */
    override fun onResume() {
        super.onResume()
        Log.d("HostStandbyActivity", "onResume")
//        BluetoothFunction.getInstance().connect()
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
//        if (null != BluetoothFunction.getInstance().mBluetoothService) {
//            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
//            BluetoothFunction.getInstance().mBluetoothService = null
//        }
    }
    /** **********************************************************************
     * onDestroy
     * ・Bluetoothデバイスを切断
     * @author 中田　桂介
     * ********************************************************************** */
    override fun onDestroy() {
        super.onDestroy()
        Log.d("HostStandbyActivity", "onDestroy")
//        if (null != BluetoothFunction.getInstance().mBluetoothService) {
//            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
//            BluetoothFunction.getInstance().mBluetoothService = null
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sutamina_menu, menu)
        stamina1 = menu?.findItem(R.id.menu_item1)
        stamina2 = menu?.findItem(R.id.menu_item2)
        stamina3 = menu?.findItem(R.id.menu_item3)

        val data: SharedPreferences =
            application.getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)

        if(data.getBoolean("staminaFirst", true)){
            Log.d("MainActivity","onCreateOptionsMenu/staminaFirst")
            stamina1?.setIcon(R.drawable.bolg_stamina_on)
        }else{
            stamina1?.setIcon(R.drawable.bolg_stamina_off)
        }
        if(data.getBoolean("staminaSecond", true)){
            Log.d("MainActivity","onCreateOptionsMenu/staminaSecond")
            stamina2?.setIcon(R.drawable.bolg_stamina_on)
        }else{
            stamina2?.setIcon(R.drawable.bolg_stamina_off)
        }
        if(data.getBoolean("staminaThird", true)){
            Log.d("MainActivity","onCreateOptionsMenu/staminaThird")
            stamina3?.setIcon(R.drawable.bolg_stamina_on)
        }else{
            stamina3?.setIcon(R.drawable.bolg_stamina_off)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val data: SharedPreferences = application.getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = data.edit()
        when(item?.itemId) {
            R.id.menu_item1 -> {
                Log.d("MainActivity","onOptionsItemSelected/menu_item1")
                item.setIcon(R.drawable.bolg_stamina_off) // Timer起動トリガー
                // stamina state off
                editor?.putBoolean("staminaFirst", false)
            }
            R.id.menu_item2 -> {
                Log.d("MainActivity","onOptionsItemSelected/menu_item2")
                item.setIcon(R.drawable.bolg_stamina_off)
                editor?.putBoolean("staminaSecond", false)
            }
            R.id.menu_item3 -> {
                Log.d("MainActivity","onOptionsItemSelected/menu_item3")
                item.setIcon(R.drawable.bolg_stamina_off)
                editor?.putBoolean("staminaThird", false)
            }
            R.id.add_stamina -> {
                Toast.makeText(applicationContext, "RoomID : " + data.getLong("room_id",0L).toString(), Toast.LENGTH_LONG).show()
                // メニューの再作成するように設定する
//                editor?.putBoolean("staminaFirst", true)
//                editor?.putBoolean("staminaSecond", true)
//                editor?.putBoolean("staminaThird", true)
//                editor?.apply()
//                invalidateOptionsMenu()
                return true
            }
        }
        editor?.apply()
        timer.start()
        return super.onOptionsItemSelected(item!!)
    }
}
