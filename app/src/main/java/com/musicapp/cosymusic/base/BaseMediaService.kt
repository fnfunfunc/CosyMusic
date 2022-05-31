package com.musicapp.cosymusic.base

import android.app.Service

/**
 * @author Eternal Epoch
 * @date 2022/5/30 19:57
 */
abstract class BaseMediaService: Service() {

    companion object{
        const val CHANNEL_ID = "Cosy Music channel id"

        const val CODE_PREVIOUS = 1 //播放上一首
        const val CODE_PLAY_OR_PAUSE = 2 //暂停或播放
        const val CODE_NEXT = 3 //播放下一首

        const val START_FOREGROUND_ID = 666
    }

    override fun onCreate() {
        super.onCreate()
        initMediaSession()
    }

    protected open fun initMediaSession() { }
}