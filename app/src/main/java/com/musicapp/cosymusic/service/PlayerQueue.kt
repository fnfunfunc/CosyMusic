package com.musicapp.cosymusic.service

import androidx.lifecycle.MutableLiveData
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.StandardMusicResponse
import com.musicapp.cosymusic.room.entity.PlayQueueData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    val currentPlayPosition = MutableLiveData(-1)

    //常规播放模式下的队列
    private val normalQueue = MutableLiveData<MutableList<StandardMusicResponse.StandardMusicData>>().also {
        it.value = mutableListOf()
    }

    fun saveNormal(musicList: MutableList<StandardMusicResponse.StandardMusicData>){
        normalQueue.value = musicList
        normal(musicList)
    }

    //正常播放模式
    private fun normal(musicList: MutableList<StandardMusicResponse.StandardMusicData>){
        currentQueue.value?.clear()
        currentQueue.value?.addAll(musicList)
        savePlayQueueToDatabase()
    }

    /**
     * 将歌单保存到数据库中
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun savePlayQueueToDatabase(){
        GlobalScope.launch {
            App.appDatabase.playQueueDao().run {
                loadAll().forEach {
                    deleteById(it.songData.id)
                }
                currentQueue.value?.let {
                    it.forEach { standardMusicData -> insert(PlayQueueData(standardMusicData)) }
                }
            }
        }
    }

    fun getPrev(): StandardMusicResponse.StandardMusicData?{
        val size = currentQueue.value?.size
        if(size != null && size != 0){
            val prevPosition = if(currentPlayPosition.value != 0)((currentPlayPosition.value ?:0) - 1) % size
            else size - 1
            currentPlayPosition.value = prevPosition
            return currentQueue.value?.get(prevPosition)
        }
        return null
    }

    fun getNext(): StandardMusicResponse.StandardMusicData?{
        val size = currentQueue.value?.size
        if(size != null && size != 0){
            val nextPosition = ((currentPlayPosition.value ?:0) + 1) % size
            currentPlayPosition.value = nextPosition
            return currentQueue.value?.get(nextPosition)
        }
        return null
    }

    fun addToNextPlay(musicData: StandardMusicResponse.StandardMusicData){
        currentQueue.value?.remove(musicData)   //如果它在目前的播放队列中，那么先将其删除
        currentQueue.value?.add((currentPlayPosition.value ?: 0) + 1, musicData)
        savePlayQueueToDatabase()
    }
}