package com.musicapp.cosymusic.base

import android.app.Service

/**
 * @author Eternal Epoch
 * @date 2022/5/30 19:57
 */
abstract class BaseMediaService: Service() {

    override fun onCreate() {
        super.onCreate()
        initMediaSession()
    }

    protected open fun initMediaSession() { }
}