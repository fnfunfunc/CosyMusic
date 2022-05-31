package com.musicapp.cosymusic.application

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tencent.mmkv.MMKV
import com.musicapp.cosymusic.model.netease.MusicResponse.MusicData
import com.musicapp.cosymusic.network.Repository
import com.musicapp.cosymusic.service.PlayerService
import com.musicapp.cosymusic.service.PlayerServiceConnection
import com.musicapp.cosymusic.util.LogUtil

/**
 * @author Eternal Epoch
 * @date 2022/5/26 21:24
 */
class App: Application() {

    companion object{
        @Suppress("StaticFieldLeak")
        lateinit var context: Context
        lateinit var mmkv: MMKV
        val playState = MutableLiveData(false)
        val playSongData = MutableLiveData<MusicData>()

        val playSongId = MutableLiveData(0L)
        val playQueue = mutableListOf<MusicData>()
        var playPositionInQueue = -1

        val playerController = MutableLiveData<PlayerService.PlayerController?>()

        val playerServiceConnection by lazy { PlayerServiceConnection() }

        private val musicId = MutableLiveData<Long>()

        val musicSourceResponse = Transformations.switchMap(musicId){ id ->
            Repository.getMusicSourceById(id)
        }

        fun getMusicSourceById(id: Long){
            musicId.value = id
        }

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
        bindService(intent, playerServiceConnection, BIND_AUTO_CREATE)
    }
}