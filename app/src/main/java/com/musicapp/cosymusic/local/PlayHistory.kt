package com.musicapp.cosymusic.local

import android.os.Parcelable
import com.musicapp.cosymusic.application.App
import kotlinx.parcelize.Parcelize
import com.musicapp.cosymusic.model.netease.MusicResponse.MusicData
import com.musicapp.cosymusic.util.Config
import com.musicapp.cosymusic.util.LogUtil

/**
 * @author Eternal Epoch
 * @date 2022/5/31 17:02
 */

//播放历史单例类
object PlayHistory {


    private var playHistoryData = PlayHistoryData(mutableListOf())


    fun addPlayHistory(musicData: MusicData){
        //如果不在先前的播放历史中，则直接添加
        if(musicData !in playHistoryData.list){
            playHistoryData.list.add(0, musicData)
        }else{
            //如果在，则将之前的移除，并重新加到首项
            playHistoryData.list.remove(musicData)
            playHistoryData.list.add(0 ,musicData)
        }
        App.mmkv.encode(Config.PLAY_HISTORY, playHistoryData)
    }

    fun readPlayHistory(): MutableList<MusicData>{
        playHistoryData = App.mmkv.decodeParcelable(Config.PLAY_HISTORY, PlayHistoryData::class.java)
            ?: PlayHistoryData(mutableListOf())
        return playHistoryData.list
    }

    fun clearPlayHistory(){
        playHistoryData.list.clear()
        App.mmkv.encode(Config.PLAY_HISTORY, playHistoryData)
    }

    @Parcelize
    data class PlayHistoryData(
        val list: MutableList<MusicData>
    ): Parcelable

}