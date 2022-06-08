package com.musicapp.cosymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.musicapp.cosymusic.network.Repository

/**
 * @author Eternal Epoch
 * @date 2022/5/29 15:30
 */
class SearchViewModel: ViewModel() {

    private val searchMusicLiveData = MutableLiveData<String>()

    val musicResultLiveData = Transformations.switchMap(searchMusicLiveData){ keywords ->
        Repository.getSearchResult(keywords)
    }

    fun getSearchResponse(keywords: String){
        searchMusicLiveData.value = keywords
    }

    private val hotSearchLiveData = MutableLiveData<Any?>()

    val hotSearchResultData = Transformations.switchMap(hotSearchLiveData){
        Repository.getHotSearchResult()
    }

    fun getHotSearchResponse(){
        hotSearchLiveData.value = hotSearchLiveData.value
    }

    private val searchTextLiveData = MutableLiveData<String>()

    val searchSuggestData = Transformations.switchMap(searchTextLiveData){ keywords ->
        Repository.getSearchSuggest(keywords)
    }

    fun getSearchSuggest(keywords: String){
        searchTextLiveData.value = keywords
    }

}