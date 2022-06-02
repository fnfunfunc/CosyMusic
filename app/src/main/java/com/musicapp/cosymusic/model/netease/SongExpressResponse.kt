package com.musicapp.cosymusic.model.netease


/**
 * @author Eternal Epoch
 * @date 2022/6/1 23:13
 */
data class SongExpressResponse(val code: Int, val data: List<SongExpressData>){

    data class SongExpressData(val id: Long ,val name: String, val fee: Int, val duration: Int,
    val artists: List<StandardMusicResponse.ArtistInfo>, val album: StandardMusicResponse.AlbumInfo)

}
