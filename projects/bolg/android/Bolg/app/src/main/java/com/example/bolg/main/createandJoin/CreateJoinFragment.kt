package com.example.bolg.main.createandJoin

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.bolg.R
import com.example.bolg.gameplay.GamePlayActivity


/** ----------------------------------------------------------------------
 * CreateJoinFragment
 * 部屋への参加/作成
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class CreateJoinFragment : Fragment(){

    // Fragmentが初めてUIを描画する時にシステムが呼び出す
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_main,container,false)

        val application: Application = requireNotNull(this.activity).application
        val viewModelFactory = CreateJoinViewModelFactory(application)
        val createJoinViewModel:CreateJoinViewModel = ViewModelProviders.of(this,viewModelFactory).get(CreateJoinViewModel::class.java)

        // Button widget Setting
        val joinBtn:ImageButton = view.findViewById(R.id.join_btn)
        val createBtn:ImageButton = view.findViewById(R.id.create_btn)

        // 参加
        joinBtn.setOnClickListener {
            createJoinViewModel.joinDialog(view)
        }

        // 部屋生成
        createBtn.setOnClickListener {
            createJoinViewModel.create(view)
        }
        return view
    }
}