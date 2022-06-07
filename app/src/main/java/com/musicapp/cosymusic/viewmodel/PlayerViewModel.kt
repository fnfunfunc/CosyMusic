package com.musicapp.cosymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.data.LyricViewData
import com.musicapp.cosymusic.network.Repository

/**
 * @author Eternal Epoch
 * @date 2022/5/30 21:07
 */
class PlayerViewModel: ViewModel() {

    val duration = MutableLiveData<Int>().also {
        it.value = App.playerController.value?.getDuration()
    }

    val progress = MutableLiveData<Int>().also {
        it.value = App.playerController.value?.currentPosition
    }

    fun changePlayState(){
        App.playerController.value?.changePlayState()
    }

    fun refreshProgress(){
        progress.postValue(App.playerController.value?.currentPosition)
    }

    fun playPrev() = App.playerController.value?.playPrev()

    fun playNext() = App.playerController.value?.playNext()

    private val musicId = MutableLiveData<Long>()

    val lyricLiveData = Transformations.switchMap(musicId){ id ->
        Repository.getLyricResponse(id)
    }

    fun getLyricData(id: Long){
        musicId.value = id
    }

}