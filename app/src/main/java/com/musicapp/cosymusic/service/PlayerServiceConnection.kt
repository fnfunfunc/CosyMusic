package com.musicapp.cosymusic.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.MusicResponse
import com.musicapp.cosymusic.util.Config
import com.musicapp.cosymusic.util.LogUtil
import com.musicapp.cosymusic.util.runOnMainThread
import kotlin.concurrent.thread

/**
 * @author Eternal Epoch
 * @date 2022/5/30 19:31
 */
class PlayerServiceConnection: ServiceConnection {

    override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
        App.playerController.value = binder as PlayerService.PlayerController
        thread {
            //恢复songData
            val recoverMusicData = App.mmkv.decodeParcelable(Config.SERVICE_CURRENT_SONG,
                MusicResponse.MusicData::class.java)
            recoverMusicData?.let { musicData ->
                runOnMainThread{
                    App.playerController.value?.let {
                        it.musicData.value = musicData
                    }
                }
            }
        }
    }

    /**
     * Service意外断开
     */
    override fun onServiceDisconnected(p0: ComponentName?) {
        App.playerController.value = null
    }

}