package com.musicapp.cosymusic.application

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.musicapp.cosymusic.room.database.AppDatabase
import com.tencent.mmkv.MMKV
import com.musicapp.cosymusic.service.MusicService
import com.musicapp.cosymusic.service.MusicServiceConnection

/**
 * @author Eternal Epoch
 * @date 2022/5/26 21:24
 */
class App: Application() {

    companion object{
        @Suppress("StaticFieldLeak")
        lateinit var context: Context
        lateinit var mmkv: MMKV

        val playerController = MutableLiveData<MusicService.PlayerController?>()

        val musicServiceConnection by lazy { MusicServiceConnection() }

        lateinit var appDatabase: AppDatabase

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        //初始化MMKV的根目录
        MMKV.initialize(this)
        mmkv = MMKV.defaultMMKV()!!

        appDatabase = AppDatabase.getDatabase(this)

        startPlayerService()
    }



    private fun startPlayerService(){
        val intent = Intent(this, MusicService::class.java)
        startForegroundService(intent)
        bindService(intent, musicServiceConnection, BIND_AUTO_CREATE)
    }
}