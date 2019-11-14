package com.example.bolg.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bolg.R
import com.example.bolg.view.fragment.MainFragment
import com.example.bolg.view.fragment.TitleFtagment
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.carDetailContainer,
                TitleFtagment()
            )
            .commit()

        if(savedInstanceState == null){

            var x = 0

            var timerCallback1: TimerTask.() -> Unit = {
                if(x++ == 4){
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.carDetailContainer,
                            MainFragment()
                        )
                        .commit()
                    this.cancel()
                }
            }
            Timer().schedule(0, 1000, timerCallback1)

        }
    }
}
