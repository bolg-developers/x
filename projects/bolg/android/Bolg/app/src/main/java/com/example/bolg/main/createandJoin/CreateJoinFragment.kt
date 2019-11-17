package com.example.bolg.main.createandJoin

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.bolg.R


class CreateJoinFragment : Fragment(){

    // Fragmentが初めてUIを描画する時にシステムが呼び出す
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_main,container,false)

        val application: Application = requireNotNull(this.activity).application

        val viewModelFactory = CreateJoinViewModelFactory(application)

        val createJoinViewModel = ViewModelProviders.of(this,viewModelFactory).get(CreateJoinViewModel::class.java)

        // Button widget Setting
        val joinBtn = view.findViewById<Button>(R.id.join_btn)
        val createBtn = view.findViewById<Button>(R.id.create_btn)

        // 参加
        joinBtn.setOnClickListener { createJoinViewModel.joinDialog(view) }

        // 部屋生成
        createBtn.setOnClickListener { createJoinViewModel.create(view) }

        return view
    }
}