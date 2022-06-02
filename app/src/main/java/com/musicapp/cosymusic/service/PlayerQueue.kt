package com.musicapp.cosymusic.service

import androidx.lifecycle.MutableLiveData
import com.musicapp.cosymusic.model.netease.StandardMusicResponse

/**
 * @author Eternal Epoch
 * @date 2022/5/31 8:01
 */

object PlayerQueue {

    //当前的播放队列
    val currentQueue = MutableLiveData<MutableList<StandardMusicResponse.StandardMusicData>>().also {
        it.value = mutableListOf()
    }

    //当前播放歌曲在队列中的位置
    val currentPosition = MutableLiveData(-1)

    //常规播放模式下的队列
    private val normalQueue = MutableLiveData<MutableList<StandardMusicResponse.StandardMusicData>>().also {
        it.value = mutableListOf()
    }

    fun saveNormal(musicList: MutableList<StandardMusicResponse.StandardMusicData>){
        normalQueue.value = musicList
        normal()
    }

    //正常播放模式
    fun normal(){
        currentQueue.value = normalQueue.value
    }

    fun getPrev(): StandardMusicResponse.StandardMusicData?{
        val size = currentQueue.value?.size
        if(size != null && size != 0){
            val prevPosition = if(currentPosition.value != 0)((currentPosition.value ?:0) - 1) % size
            else size - 1
            currentPosition.value = prevPosition
            return currentQueue.value?.get(prevPosition)
        }
        return null
    }

    fun getNext(): StandardMusicResponse.StandardMusicData?{
        val size = currentQueue.value?.size
        if(size != null && size != 0){
            val nextPosition = ((currentPosition.value ?:0) + 1) % size
            currentPosition.value = nextPosition
            return currentQueue.value?.get(nextPosition)
        }
        return null
    }
}