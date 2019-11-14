package com.example.bolg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.carDetailContainer, TitleFtagment())
            .commit()

        if(savedInstanceState == null){

            //val mHandler = Handler()
            var x = 0

            var timerCallback1: TimerTask.() -> Unit = {
                var str = Integer.toString(x)
                //mHandler.post{messageView.text = str}
                //messageView.text = str
                System.out.println(str)
                x++
                if(x == 4){
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.carDetailContainer, MainFragment())
                        .commit()
                }
                //this.cancel()
            }
            Timer().schedule(0, 1000, timerCallback1)

        }
    }
}
