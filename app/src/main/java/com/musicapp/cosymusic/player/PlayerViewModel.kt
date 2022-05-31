package com.musicapp.cosymusic.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.musicapp.cosymusic.application.App

/**
 * @author Eternal Epoch
 * @date 2022/5/30 21:07
 */
class PlayerViewModel: ViewModel() {

    val duration = MutableLiveData<Int>().also {
        it.value = App.playerController.value?.duration
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
}