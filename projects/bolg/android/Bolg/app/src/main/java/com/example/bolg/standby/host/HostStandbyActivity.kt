package com.example.bolg.standby.host

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bolg.adapter.StandbyRecyclerAdapter
import com.example.bolg.R
import com.example.bolg.data.ListData

/**
 * HostStandbyActivity
 * 部屋生成者の待機画面
 * ・参加ユーザーのリスト表示
 * ・ゲームルールの決定画面のView
 * */
class HostStandbyActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var hostStandbyViewModel: HostStandbyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_standby)

        /** widget init **/
        val progress    : ProgressBar =  findViewById(R.id.pairing_progress)
        val hostpairing : Button = findViewById(R.id.host_pairing)
        val kakinbullet : Button = findViewById(R.id.host_kakin_bullet)
        val item        : Button = findViewById(R.id.host_item_btn)
        val inventory   : Button = findViewById(R.id.host_inventory_btn)
        val start       : Button = findViewById(R.id.host_ready_btn)
        val rulespinner : Spinner = findViewById(R.id.host_game_rule_spinner)
        val joinuser    : RecyclerView = findViewById(R.id.standby_recycler_view)

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
            rulespinner.adapter = adapter
        }
        rulespinner.onItemSelectedListener = this

        /** viewModel init **/
        val application: Application = requireNotNull(this).application
        val viewModelFactoryHost: HostStandbyViewModelFactory =
            HostStandbyViewModelFactory(application)
        hostStandbyViewModel = ViewModelProviders.of(this,viewModelFactoryHost).get(HostStandbyViewModel::class.java)

        /**************************ViewModelに分割したい*********************************************************/
        // LayoutManagerの設定
        val layoutManager = LinearLayoutManager(this)
        joinuser.layoutManager = layoutManager

        // Adapterの設定
        // 試しに入れているだけ
        var sampleList = mutableListOf<ListData>()
        for (i in 0..10) {
            sampleList.add(i, ListData("hasegawa${i}"))
        }
        val adapter = StandbyRecyclerAdapter(sampleList)
        joinuser.adapter = adapter
        // 区切り線の表示
        joinuser.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        /******************************************************************************************************/


        /** onClick **/
        // 課金ボタンON/OFF
        kakinbullet.setOnClickListener {
            if (hostStandbyViewModel.kakinBulletState){ Log.d("button","kakinbullet:OFF") }
            else {
                Log.d("button", "kakinbullet:ON") }
            hostStandbyViewModel.kakinBulletState = !hostStandbyViewModel.kakinBulletState
        }

        // アイテムボタンON/OFF
        item.setOnClickListener {
            if (hostStandbyViewModel.itemState){ Log.d("button","item:OFF") }
            else {
                Log.d("button", "item:ON") }
            hostStandbyViewModel.itemState = !hostStandbyViewModel.itemState
        }

        // ペアリング
        hostpairing.setOnClickListener {
            progress.visibility = ProgressBar.VISIBLE
            Log.d("button", "progress:ON")
            if(hostStandbyViewModel.pairing()){
                progress.visibility = ProgressBar.INVISIBLE
                Log.d("button", "progress:OFF")
            }
        }

        // ゲームスタート
        start.setOnClickListener { hostStandbyViewModel.startGame(this) }

        // インベントリ
        inventory.setOnClickListener { Toast.makeText(applicationContext, "未実装", Toast.LENGTH_LONG).show() }
    }

    /** **********************************************************************
     * onItemSelected
     * @param parent
     * @param view
     * @param position
     * @param id
     * 選択ItemをViewに反映させる
     * ********************************************************************** */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val spinnerParent = parent as Spinner
        val item = spinnerParent.selectedItem as String

        hostStandbyViewModel.updateGameRule(item)

    }

    /** **********************************************************************
     * onNothingSelected
     * @param parent
     * ********************************************************************** */
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}