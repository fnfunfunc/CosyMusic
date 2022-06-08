package com.musicapp.cosymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.musicapp.cosymusic.network.Repository

/**
 * @author Eternal Epoch
 * @date 2022/6/7 14:59
 */
class ArtistViewModel: ViewModel() {

    private val artistId = MutableLiveData<Long>()


    val artistInfoResult = Transformations.switchMap(artistId){ id ->
        Repository.getArtistInfoResult(id)
    }

    val artistSingleSongResult = Transformations.switchMap(artistId){ id ->
        Repository.getArtistSingleSongResult(id)
    }

    val artistAlbumResult = Transformations.switchMap(artistId){ id ->
        Repository.getArtistAlbumResult(id)
    }

    val descResult = Transformations.switchMap(artistId){ id ->
        Repository.getArtistDescResult(id)
    }

    fun getArtistAllInfo(id: Long){
        artistId.value = id
    }


}