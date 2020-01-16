package com.example.bolg

import android.app.Application
import android.content.Context


/** ----------------------------------------------------------------------
 * MyApplication
 * ・どこからでもContextを呼び出す
 * @author 中田　桂介
 * ---------------------------------------------------------------------- */
class MyApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

//        val context: Context = applicationContext()

    }

}