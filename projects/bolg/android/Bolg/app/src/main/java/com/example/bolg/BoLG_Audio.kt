package com.example.bolg

import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log

/** ----------------------------------------------------------------------
 * BoLG_Audio
 * @brief 効果音の管理、再生
 * @author 山口　雄大
 * ---------------------------------------------------------------------- */
class BoLG_Audio private constructor() {
    companion object {
        private var INSTANCE: BoLG_Audio ? = null
        fun getInstance(): BoLG_Audio {
            if(INSTANCE == null){
                INSTANCE = BoLG_Audio()
            }
            return INSTANCE!!
        }
    }

    enum class AudioID {
        // 被弾
        HIT,

        // ピピ
        SHOT
    }

    private data class SoundData(
        var resourceID : Int,
        var soundID  : Int,
        var streamID : Int
    )

    private var streamHandles : Array<SoundData> = arrayOf(
        SoundData(R.raw.hidan,0,0),
        SoundData(R.raw.zyusei, 0,0)
    )

    private var soundPool: SoundPool

    init{
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(AudioID.values().size)
            .build()

        val context = MyApplication.applicationContext()

        streamHandles.forEach { itr->
            itr.soundID = soundPool.load(context,itr.resourceID,1)
        }
    }

    fun play(ID : AudioID, loop : Int ,rate : Float){
        stop(ID)
        streamHandles[ID.ordinal].streamID = soundPool.play(streamHandles[ID.ordinal].soundID,1.0f,1.0f,0,loop,rate)
    }

    fun stop(ID : AudioID){
        soundPool.stop(streamHandles[ID.ordinal].streamID)
    }
}
