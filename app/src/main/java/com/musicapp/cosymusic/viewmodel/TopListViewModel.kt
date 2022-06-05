package com.musicapp.cosymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.musicapp.cosymusic.network.Repository

/**
 * @author Eternal Epoch
 * @date 2022/6/5 0:25
 */
class TopListViewModel: ViewModel() {

    private val topList = MutableLiveData<Any?>()

    val topListResponse = Transformations.switchMap(topList){
        Repository.getTopListResponse()
    }

    fun getTopListResponse(){
        topList.value = topList.value
    }

}