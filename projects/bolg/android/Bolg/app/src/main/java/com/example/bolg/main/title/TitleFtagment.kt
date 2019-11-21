package com.example.bolg.main.title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bolg.R

/** ----------------------------------------------------------------------
 * TitleFragment
 * メイン画面の前にLogoを表示するFragment
 * ---------------------------------------------------------------------- */
class TitleFtagment :Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_title,container,false)
    }
}