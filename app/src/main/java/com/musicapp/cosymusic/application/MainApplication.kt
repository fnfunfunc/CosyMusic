package com.musicapp.cosymusic.application

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tencent.mmkv.MMKV
import com.musicapp.cosymusic.model.netease.MusicResponse.MusicData
import com.musicapp.cosymusic.service.PlayerService

/**
 * @author Eternal Epoch
 * @date 2022/5/26 21:24
 */
class MainApplication: Application() {

    companion object{
        @Suppress("StaticFieldLeak")
        lateinit var context: Context
        lateinit var mmkv: MMKV
        val playState = MutableLiveData(false)
        val playSongData = MutableLiveData<MusicData>()
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        //初始化MMKV的根目录
        MMKV.initialize(this)
        mmkv = MMKV.defaultMMKV()!!

        startPlayerService()
    }

    private fun startPlayerService(){
        val intent = Intent(this, PlayerService::class.java)
        startForegroundService(intent)
    }
}