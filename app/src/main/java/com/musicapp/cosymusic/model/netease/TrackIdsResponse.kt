package com.musicapp.cosymusic.model.netease

import com.google.gson.annotations.SerializedName
import com.musicapp.cosymusic.model.netease.standard.StdAlbumInfo
import com.musicapp.cosymusic.model.netease.standard.StdArtistInfo

/**
 * @author Eternal Epoch
 * @date 2022/6/3 21:54
 */
data class TrackIdsResponse(val code: Int, val songs: List<MenuMusicData>){

    data class MenuMusicData(
        val id: Long,      //歌曲Id
        val name: String,   //歌曲名称
        @SerializedName("ar") val artists: List<StdArtistInfo>?,
        @SerializedName("al") val album: StdAlbumInfo,
        val pop: Int?,     //热度
        val fee: Int,
        @SerializedName("dt") val duration: Int
    )

}
