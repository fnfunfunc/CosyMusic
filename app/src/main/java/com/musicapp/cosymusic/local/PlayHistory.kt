package com.musicapp.cosymusic.local

import android.os.Parcelable
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.standard.StdMusicData
import kotlinx.parcelize.Parcelize
import com.musicapp.cosymusic.util.KString

/**
 * @author Eternal Epoch
 * @date 2022/5/31 17:02
 */

//播放历史单例类
object PlayHistory {


    private var playHistoryData = PlayHistoryData(mutableListOf())


    fun addPlayHistory(musicData: StdMusicData){
        //如果不在先前的播放历史中，则直接添加
        if(musicData !in playHistoryData.list){
            playHistoryData.list.add(0, musicData)
        }else{
            //如果在，则将之前的移除，并重新加到首项
            playHistoryData.list.remove(musicData)
            playHistoryData.list.add(0 ,musicData)
        }
        App.mmkv.encode(KString.PLAY_HISTORY, playHistoryData)
    }

    fun readPlayHistory(): MutableList<StdMusicData>{
        playHistoryData = App.mmkv.decodeParcelable(KString.PLAY_HISTORY, PlayHistoryData::class.java)
            ?: PlayHistoryData(mutableListOf())
        return playHistoryData.list
    }

    fun clearPlayHistory(){
        playHistoryData.list.clear()
        App.mmkv.encode(KString.PLAY_HISTORY, playHistoryData)
    }

    @Parcelize
    data class PlayHistoryData(
        val list: MutableList<StdMusicData>
    ): Parcelable

}