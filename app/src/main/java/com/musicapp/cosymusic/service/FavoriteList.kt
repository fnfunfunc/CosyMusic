package com.musicapp.cosymusic.service

import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.StandardMusicResponse.StandardMusicData
import com.musicapp.cosymusic.room.entity.FavoriteListData
import kotlinx.coroutines.*

/**
 * @author Eternal Epoch
 * @date 2022/6/5 21:35
 */
object FavoriteList {

    val fList = mutableListOf<StandardMusicData>()


    fun addToFavoriteList(musicData: StandardMusicData){
        fList.add(musicData)
        saveToDatabase()
    }

    fun addAllToFavoriteList(musicDataList: List<StandardMusicData>){
        fList.addAll(musicDataList)
        saveToDatabase()
    }

    fun isInFavoriteList(musicData: StandardMusicData) = musicData in fList

    fun removeFromFavoriteList(musicData: StandardMusicData){
        fList.remove(musicData)
        saveToDatabase()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveToDatabase(){
        GlobalScope.launch{
            App.appDatabase.favoriteListDao().run {
                loadAll().forEach{
                    deleteById(it.musicData.id)
                }
                fList.forEach { standardMusicData ->
                    insert(FavoriteListData(standardMusicData))
                }
            }
        }
    }

}
