package com.musicapp.cosymusic.application

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.musicapp.cosymusic.room.database.AppDatabase
import com.tencent.mmkv.MMKV
import com.musicapp.cosymusic.service.PlayerService
import com.musicapp.cosymusic.service.PlayerServiceConnection

/**
 * @author Eternal Epoch
 * @date 2022/5/26 21:24
 */
class App: Application() {

    companion object{
        @Suppress("StaticFieldLeak")
        lateinit var context: Context
        lateinit var mmkv: MMKV

        val playerController = MutableLiveData<PlayerService.PlayerController?>()

        val playerServiceConnection by lazy { PlayerServiceConnection() }

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
        val intent = Intent(this, PlayerService::class.java)
        startForegroundService(intent)
        bindService(intent, playerServiceConnection, BIND_AUTO_CREATE)
    }
}