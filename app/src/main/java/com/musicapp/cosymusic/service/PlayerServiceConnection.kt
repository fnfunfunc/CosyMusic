package com.musicapp.cosymusic.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.StandardMusicResponse
import com.musicapp.cosymusic.room.entity.toStandardList
import com.musicapp.cosymusic.util.KString
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
            //恢复musicData
            val recoverMusicData = App.mmkv.decodeParcelable(KString.SERVICE_CURRENT_SONG,
                StandardMusicResponse.StandardMusicData::class.java)
            //恢复playQueue
            val recoverPlayQueue = App.appDatabase.playQueueDao().loadAll().toStandardList()
            //恢复currentPlayPosition
            val recoverCurrentPlayPosition = App.mmkv.decodeInt(KString.CURRENT_PLAY_POSITION)
            recoverMusicData?.let { musicData ->
                runOnMainThread{
                    App.playerController.value?.let {
                        it.musicData.value = musicData
                        it.setDataSource(App.context, Uri.parse("https://music.163.com" +
                                "/song/media/outer/url?id=${musicData.id}.mp3"))
                        it.prepareAsync()
                        it.savePlayList(recoverPlayQueue.toMutableList())
                        it.setCurrentPlayPosition(recoverCurrentPlayPosition)
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