package com.musicapp.cosymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.musicapp.cosymusic.network.Repository

/**
 * @author Eternal Epoch
 * @date 2022/6/8 10:22
 */
class AlbumViewModel: ViewModel() {

    private val albumId = MutableLiveData<Long>()

    val albumDataResult = Transformations.switchMap(albumId){ id ->
        Repository.getArtistAlbumDataResult(id)
    }


    fun getAlbumData(id: Long){
        albumId.value = id
    }

}