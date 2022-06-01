package com.musicapp.cosymusic.local

import android.os.Parcelable
import android.util.Log
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.util.Config
import com.musicapp.cosymusic.util.LogUtil
import kotlinx.parcelize.Parcelize

/**
 * @author Eternal Epoch
 * @date 2022/6/1 8:41
 */

//搜索历史单例类
object SearchHistory {

    private var searchHistoryData = SearchHistoryData(mutableListOf())

    fun addSearchHistory(searchText: String){
        if(searchText !in searchHistoryData.list){
            searchHistoryData.list.add(0, searchText)
        }else{
            searchHistoryData.list.remove(searchText)
            searchHistoryData.list.add(0, searchText)
        }
        LogUtil.e("SearchHistory", searchHistoryData.list.toString())
        App.mmkv.encode(Config.SEARCH_HISTORY, searchHistoryData)
    }

    fun readSearchHistory(): MutableList<String>{
        searchHistoryData = App.mmkv.decodeParcelable(Config.SEARCH_HISTORY, SearchHistoryData::class.java)
            ?: SearchHistoryData(mutableListOf())
        return searchHistoryData.list
    }

    fun clearSearchHistory(){
        searchHistoryData.list.clear()
        App.mmkv.encode(Config.SEARCH_HISTORY, searchHistoryData)
    }

    @Parcelize
    data class SearchHistoryData(
        val list: MutableList<String>
    ): Parcelable

}