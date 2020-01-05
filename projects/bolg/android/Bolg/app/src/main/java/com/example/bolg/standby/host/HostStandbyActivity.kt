package com.example.bolg.standby.host

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.Image
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.GrpcTask
import com.example.bolg.adapter.StandbyRecyclerAdapter
import com.example.bolg.R
import com.example.bolg.bluetooth.BluetoothFunction
import com.example.bolg.data.ListData
import com.example.bolg.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_standby)

        // root view
        val decorView = window.decorView

        /** widget init **/
        val userId: TextView = findViewById(R.id.host_user_id)
        val joinMember: TextView = findViewById(R.id.host_join_num)
        val progress: ProgressBar = findViewById(R.id.host_pairing_progress)
        val hostPairing: ImageButton = findViewById(R.id.host_pairing)
        val kakinBullet: ImageButton = findViewById(R.id.host_kakin_bullet)
        val item: ImageButton = findViewById(R.id.host_item_btn)
        val inventory: ImageButton = findViewById(R.id.host_inventory_btn)
        val start: ImageButton = findViewById(R.id.host_start_btn)
        val ruleSpinner: Spinner = findViewById(R.id.host_game_rule_spinner)
        val joinUser: RecyclerView = findViewById(R.id.host_standby_recycler_view)

        /** SharedPreferences **/
        val data: SharedPreferences = getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = data.edit()
        userId.text = "${data.getString("token", "error")}"
        Log.d("createAndJoinRoomTask", "token ->" + data.getString("token", "取得出来てない"))

        /** Toolbar init **/
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

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

        /** CountDownTimer init **/
        timer = object : CountDownTimer(data.getLong("nowTimer",0), 1000){
            override fun onTick(millisUntilFinished: Long) {
                // "00:00:00"の方式で表示する。
                toolbar.title = String.format(
                    Locale.getDefault(),"%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)%60,
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)%60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60)
            }

            override fun onFinish() {
                toolbar.title  = "終了"
                stamina1?.setIcon(R.drawable.favorite)
                editor?.putBoolean("staminaFirst", true)
                editor?.putBoolean("staminaSecond", true)
                editor?.putBoolean("staminaThird", true)
                editor?.apply()
            }
        }.start()

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
        // 入室者数
        hostStandbyViewModel.readyPlayerOwner.observe(this, Observer {
        })

        /** observe init **/
        GrpcTask.getInstance(application).joinUserNum.observe(this, Observer { joinNum ->
            Log.d("Host","${joinNum}人が参加しています")
            joinMember.text = joinNum.toString()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sutamina_menu, menu)
        stamina1 = menu?.findItem(R.id.menu_item1)
        stamina2 = menu?.findItem(R.id.menu_item2)
        stamina3 = menu?.findItem(R.id.menu_item3)

        val data: SharedPreferences =
            application.getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)

        if(data.getBoolean("staminaFirst", true)){
            Log.d("MainActivity","onCreateOptionsMenu/staminaFirst")
            stamina1?.setIcon(R.drawable.favorite)
        }else{
            stamina1?.setIcon(R.drawable.favorite_off)
        }
        if(data.getBoolean("staminaSecond", true)){
            Log.d("MainActivity","onCreateOptionsMenu/staminaSecond")
            stamina2?.setIcon(R.drawable.favorite)
        }else{
            stamina2?.setIcon(R.drawable.favorite_off)
        }
        if(data.getBoolean("staminaThird", true)){
            Log.d("MainActivity","onCreateOptionsMenu/staminaThird")
            stamina3?.setIcon(R.drawable.favorite)
        }else{
            stamina3?.setIcon(R.drawable.favorite_off)
        }

        return super.onCreateOptionsMenu(menu)
//        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val data: SharedPreferences = application.getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = data.edit()
        when(item?.itemId) {
            R.id.menu_item1 -> {
                Log.d("MainActivity","onOptionsItemSelected/menu_item1")
                item.setIcon(R.drawable.favorite_off) // Timer起動トリガー
                // stamina state off
                editor?.putBoolean("staminaFirst", false)
                editor?.apply()
                timer.start()
            }
            R.id.menu_item2 -> {
                Log.d("MainActivity","onOptionsItemSelected/menu_item2")
                item.setIcon(R.drawable.favorite_off)
                editor?.putBoolean("staminaSecond", false)
                editor?.apply()
                timer.start()
            }
            R.id.menu_item3 -> {
                Log.d("MainActivity","onOptionsItemSelected/menu_item3")
                item.setIcon(R.drawable.favorite_off)
                editor?.putBoolean("staminaThird", false)
                editor?.apply()
                timer.start()
            }
            R.id.add_stamina -> {
                Toast.makeText(applicationContext, "スタミナ回復Dialog", Toast.LENGTH_LONG).show()
                // メニューの再作成するように設定する
                editor?.putBoolean("staminaFirst", true)
                editor?.putBoolean("staminaSecond", true)
                editor?.putBoolean("staminaThird", true)
                editor?.commit()
                invalidateOptionsMenu()
                return true
                timer.onFinish()
            }
        }
        return super.onOptionsItemSelected(item!!)
    }
}