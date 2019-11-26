package com.example.bolg.gameplay

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel

/** ----------------------------------------------------------------------
 * GamePlayViewModel
 * @param application : AndroidViewModelの引数に使用
 * @author 長谷川　勇太
 * ---------------------------------------------------------------------- */
class GamePlayViewModel(application: Application) : AndroidViewModel(application){
    private var mReadByte = ByteArray(256)


    /** **********************************************************************
     * readByte
     * @param ByteArray
     * @author 中田　桂介
     * ********************************************************************** */
    fun readByte(byteArray:ByteArray){
        mReadByte = byteArray
        Log.d("テスト" , String(mReadByte, 0 ,mReadByte.size))
        Log.d("GamePlayViewModel", "Get ByteArray")
    }
}