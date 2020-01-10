package com.example.bolg.standby.player

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.TimeUnit

/** ----------------------------------------------------------------------
 * クラス名 PlayerStandbyActivity
 * ・概要1
 * ・概要2
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
@Suppress("UNREACHABLE_CODE")
class PlayerStandbyActivity : AppCompatActivity(){
    private lateinit var playerStandbyViewModel: PlayerStandbyViewModel
    /** stamina  info init **/
    private lateinit var timer: CountDownTimer
    private var stamina1: MenuItem? = null
    private var stamina2: MenuItem? = null
    private var stamina3: MenuItem ? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_standby)

        // root view
        val decorView: View = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE

        /** widget init **/
        val userId        : TextView     = findViewById(R.id.player_user_id)
        val rule          : TextView     = findViewById(R.id.player_rule)
        val ready         : ImageButton  = findViewById(R.id.player_ready_btn)
        val playerPairing : ImageButton  = findViewById(R.id.player_pairing_btn)
        val progress      : ProgressBar  = findViewById(R.id.player_progress)
        val joinUser      : RecyclerView = findViewById(R.id.player_standby_recycler_view)


        ready.isEnabled = false


        /** viewModel**/
        val application: Application = requireNotNull(this).application
        val viewModelFactoryPlayer = PlayerStandbyViewModelFactory(application)
        playerStandbyViewModel=
            ViewModelProviders.of(this, viewModelFactoryPlayer)
                .get(PlayerStandbyViewModel::class.java)

        /** SharedPreferences **/
        val data: SharedPreferences = getSharedPreferences("RoomDataSave", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = data.edit()
        userId.text = "${data.getString("token", "error")}"


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
                toolbar.title  = "BOLG"
                stamina1?.setIcon(R.drawable.favorite)
                editor?.putBoolean("staminaFirst", true)
                editor?.putBoolean("staminaSecond", true)
                editor?.putBoolean("staminaThird", true)
                editor?.apply()
            }
        }.start()

        /** Toolbar init **/
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        /** RecyclerView init **/
        val layoutManager = LinearLayoutManager(this)
        joinUser.layoutManager = layoutManager
        // Adapterの設定
        val sampleList: MutableList<ListData> = mutableListOf()
        for (i: Int in 0..10) {
            sampleList.add(i, ListData("hasegawa${i}"))
        }
        val adapter = StandbyRecyclerAdapter(sampleList)
        joinUser.adapter = adapter
        // 区切り線の表示
        joinUser.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


        /** onClick **/
        ready.setOnClickListener {
            progress.visibility = ProgressBar.VISIBLE
            playerStandbyViewModel.setReady(data.getString("token", "error").toString(), decorView)
        }
        // ペアリング
        playerPairing.setOnClickListener {
            progress.visibility = ProgressBar.VISIBLE
            Log.d("button", "progress:ON")
            if (playerStandbyViewModel.pairing(decorView)) {
                progress.visibility = ProgressBar.INVISIBLE
                Log.d("button", "progress:OFF")
                ready.isEnabled = true
                ready.setImageResource(R.drawable.bolg_ready_on_right)

            }
        }

        /** Observe kind **/
        // ゲームルール
        playerStandbyViewModel.gameRule.observe(this, Observer { mrule ->
            rule.text = mrule
        })

        // 入室者数
        playerStandbyViewModel.readyPlayerNormal.observe(this, Observer {
        })

        // アイテムON/OFF
        playerStandbyViewModel.itemState.observe(this, Observer {
        })

        // 課金弾ON/OFF
        playerStandbyViewModel.kakinBulletState.observe(this, Observer {
        })

        GrpcTask.getInstance(application).joinUserNum.observe(this, Observer { joinNum ->
            Log.d("Host","${joinNum}人が参加しています")
//            joinMember.text = joinNum.toString()
        })

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
//        if (null != BluetoothFunction.getInstance().mBluetoothService) {
//            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
//            BluetoothFunction.getInstance().mBluetoothService = null
//        }
    }

    /** **********************************************************************
     * onStop
     * ・Bluetoothデバイスを切断
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
                item.setIcon(R.drawable.favorite_off) // Timer起動トリガー
                editor?.putBoolean("staminaSecond", false)
                editor?.apply()
                timer.start()
            }
            R.id.menu_item3 -> {
                Log.d("MainActivity","onOptionsItemSelected/menu_item3")
                item.setIcon(R.drawable.favorite_off) // Timer起動トリガー
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