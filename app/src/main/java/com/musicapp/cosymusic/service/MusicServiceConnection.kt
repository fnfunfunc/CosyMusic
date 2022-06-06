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
class MusicServiceConnection: ServiceConnection {

    override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
        App.playerController.value = binder as MusicService.PlayerController
        thread {
            //恢复musicData
            val recoverMusicData = App.mmkv.decodeParcelable(KString.SERVICE_CURRENT_SONG,
                StandardMusicResponse.StandardMusicData::class.java)
            //恢复playQueue
            val recoverPlayQueue = App.appDatabase.playQueueDao().loadAll().toStandardList()
            //恢复currentPlayPosition
            val recoverCurrentPlayPosition = App.mmkv.decodeInt(KString.CURRENT_PLAY_POSITION)
            //恢复favoriteList
            val recoverFavoriteList = App.appDatabase.favoriteListDao().loadAll().toStandardList()
            recoverMusicData?.let { musicData ->
                runOnMainThread{
                    App.playerController.value?.let {
                        it.musicData.value = musicData
                        it.setDataSource(App.context, Uri.parse("https://music.163.com" +
                                "/song/media/outer/url?id=${musicData.id}.mp3"))
                        it.prepareAsync()
                    }
                }
            }
            App.playerController.value?.let {
                runOnMainThread{
                    it.savePlayList(recoverPlayQueue.toMutableList())
                    it.setCurrentPlayPosition(recoverCurrentPlayPosition)
                    it.addAllToMyFavorite(recoverFavoriteList)
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