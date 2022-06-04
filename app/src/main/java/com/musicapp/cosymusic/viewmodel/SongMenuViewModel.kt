package com.musicapp.cosymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.musicapp.cosymusic.network.Repository

/**
 * @author Eternal Epoch
 * @date 2022/6/3 21:23
 */
class SongMenuViewModel: ViewModel() {

    private val songMenuId = MutableLiveData<Long>()

    val songMenuResponse = Transformations.switchMap(songMenuId){ id ->
        Repository.getSongMenuById(id)
    }

    fun getSongMenuById(id: Long){
        songMenuId.value = id
    }

    private val trackIds = MutableLiveData<List<Long>>()

    val menuMusicResponse = Transformations.switchMap(trackIds){ ids ->
        Repository.getMusicByTrackIds(ids)
    }

    fun getMusicByTracksId(ids: List<Long>){
        trackIds.value = ids
    }

}