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

    val musicResponseLiveData = Transformations.switchMap(searchMusicLiveData){ keywords ->
        Repository.getSearchResponse(keywords)
    }

    fun getSearchResponse(keywords: String){
        searchMusicLiveData.value = keywords
    }

    private val hotSearchLiveData = MutableLiveData<Any?>()

    val hotSearchResponseData = Transformations.switchMap(hotSearchLiveData){
        Repository.getHotSearchResponse()
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