package com.musicapp.cosymusic.model.netease

import com.musicapp.cosymusic.model.netease.standard.StdAlbumInfo
import com.musicapp.cosymusic.model.netease.standard.StdArtistInfo


/**
 * @author Eternal Epoch
 * @date 2022/6/1 23:13
 */
data class SongExpressResponse(val code: Int, val data: List<SongExpressData>){

    data class SongExpressData(val id: Long, val name: String, val fee: Int, val duration: Int,
                               val artists: List<StdArtistInfo>, val album: StdAlbumInfo)

}
