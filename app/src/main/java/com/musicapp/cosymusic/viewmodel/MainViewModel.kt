package com.musicapp.cosymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.musicapp.cosymusic.network.Repository

/**
 * @author Eternal Epoch
 * @date 2022/6/1 22:19
 */
class MainViewModel: ViewModel() {


    fun getSongExpressList(){
        chineseType.value = 7
        euAmType.value = 96
        japaneseType.value = 8
        koreanType.value = 16
    }

    private val chineseType = MutableLiveData(7)

    private val euAmType = MutableLiveData(96)

    private val japaneseType = MutableLiveData(8)

    private val koreanType = MutableLiveData(16)

    val chineseSongList = Transformations.switchMap(chineseType){ type ->
        Repository.getSongExpressList(type)
    }

    val euAmSongList = Transformations.switchMap(euAmType){ type ->
        Repository.getSongExpressList(type)
    }

    val japaneseSongList = Transformations.switchMap(japaneseType){ type ->
        Repository.getSongExpressList(type)
    }

    val koreanSongList = Transformations.switchMap(koreanType){ type ->
        Repository.getSongExpressList(type)
    }

}