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

    private val musicId = MutableLiveData<Long>()

    val musicSourceResponse = Transformations.switchMap(musicId){ id ->
        Repository.getMusicSourceById(id)
    }

    fun getMusicSourceById(id: Long){
        musicId.value = id
    }

}