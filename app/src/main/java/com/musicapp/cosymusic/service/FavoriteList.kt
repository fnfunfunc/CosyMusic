package com.musicapp.cosymusic.service

import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.standard.StdMusicData
import com.musicapp.cosymusic.room.entity.FavoriteListData
import kotlinx.coroutines.*

/**
 * @author Eternal Epoch
 * @date 2022/6/5 21:35
 */
object FavoriteList {

    val fList = mutableListOf<StdMusicData>()


    fun addToFavoriteList(musicData: StdMusicData){
        fList.add(musicData)
        saveToDatabase()
    }

    fun addAllToFavoriteList(musicDataList: List<StdMusicData>){
        fList.addAll(musicDataList)
        saveToDatabase()
    }

    fun isInFavoriteList(musicData: StdMusicData) = musicData in fList

    fun removeFromFavoriteList(musicData: StdMusicData){
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
