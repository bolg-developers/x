package com.example.bolg.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.bolg.R
import com.example.bolg.R.menu.sutamina_menu
import com.example.bolg.bluetooth.BluetoothFunction
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit
import android.content.DialogInterface
import android.app.AlertDialog
import android.app.PendingIntent.getActivity


/** ----------------------------------------------------------------------
 * MainActivity
 * TitleFragmentの一定時間表示指示クラス
 * 上記が終了するとCreateJoinFragmentの表示指示
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class MainActivity : AppCompatActivity() {

    /** stamina  info init **/
    private lateinit var timer: CountDownTimer
    private var stamina1: MenuItem? = null
    private var stamina2: MenuItem? = null
    private var stamina3: MenuItem ? = null
    private var nowTimer: Long? = null

    lateinit var mainViewModel: MainViewModel

    companion object{
        const val stamina = 20000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity","onCreate")

        // hide navigation bar, hide status bar
        val decor: View = window.decorView
        decor.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE

        val application: Application =
            requireNotNull(this).application

        val viewModelFactory =
            MainViewModelFactory(
                application,
                supportFragmentManager
            )

        mainViewModel =
            ViewModelProviders
                .of(this,viewModelFactory)
                .get(MainViewModel::class.java)

        // CreateAndJoin1画面の表示
        mainViewModel.setCreateJoin()

        // スタミナ情報の初期化
        mainViewModel.initStaminaPreferences(application)

        /** Toolbar init **/
        setSupportActionBar(toolbar)

        /** CountDownTimer init **/
        val editor: SharedPreferences.Editor? =
            application.getSharedPreferences(
                "RoomDataSave", Context.MODE_PRIVATE)
                .edit()
        timer = object : CountDownTimer(stamina, 1000){
            override fun onTick(millisUntilFinished: Long) {
                // "00:00:00"の方式で表示する。
                toolbar.title = String.format(
                    Locale.getDefault(),"%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)%60,
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)%60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60)

                // 画面遷移時値を継続させる
                nowTimer = millisUntilFinished
                editor?.putLong("nowTimer", nowTimer!!)
                editor?.apply()    // 非同期
            }

            override fun onFinish() {
                toolbar.title  = "BOLG"
                // スタミナの数を見て終了するか判断
                // スタミナ回復する
                // 判断
                stamina1?.setIcon(R.drawable.bolg_stamina_on)
                editor?.putBoolean("staminaFirst", true)
                editor?.putBoolean("staminaSecond", true)
                editor?.putBoolean("staminaThird", true)
                editor?.apply()
            }
        }

        if (null != BluetoothFunction.getInstance().mBluetoothService) {
            BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
            BluetoothFunction.getInstance().mBluetoothService = null
        }
    }

    /** Stamina display menu **/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(sutamina_menu, menu)
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
        val editor: SharedPreferences.Editor? =
            application.getSharedPreferences(
                "RoomDataSave", Context.MODE_PRIVATE)
                .edit()

        when(item?.itemId) {
            R.id.menu_item1 -> {
                Log.d("MainActivity","onOptionsItemSelected/menu_item1")
                item.setIcon(R.drawable.bolg_stamina_off) // Timer起動トリガー
                // stamina state off
                editor?.putBoolean("staminaFirst", false)
                editor?.apply()
                timer.start()
            }
            R.id.menu_item2 -> {
                Log.d("MainActivity","onOptionsItemSelected/menu_item2")
                item.setIcon(R.drawable.bolg_stamina_off)
                editor?.putBoolean("staminaSecond", false)
                editor?.apply()
                timer.start()
            }
            R.id.menu_item3 -> {
                Log.d("MainActivity","onOptionsItemSelected/menu_item3")
                item.setIcon(R.drawable.bolg_stamina_off)
                editor?.putBoolean("staminaThird", false)
                editor?.apply()
                timer.start()
            }
            R.id.add_stamina -> {
                Toast.makeText(
                    applicationContext,
                    "Roomが作られていません",
                    Toast.LENGTH_LONG)
                    .show()
                return true
            }
        }
        return super.onOptionsItemSelected(item!!)
    }

    override fun onRestart() {
        super.onRestart()
        /** SharedPreferences **/
        val data: SharedPreferences =
            getSharedPreferences(
                "RoomDataSave",
                Context.MODE_PRIVATE)

        if(data.getBoolean("loop_state",false)){
            if (null != BluetoothFunction.getInstance().mBluetoothService) {
                BluetoothFunction.getInstance().mBluetoothService!!.disconnectStart()
                BluetoothFunction.getInstance().mBluetoothService = null
            }
        }
    }
}