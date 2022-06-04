package com.musicapp.cosymusic.model.netease

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author Eternal Epoch
 * @date 2022/6/3 21:54
 */
data class TrackIdsResponse(val code: Int, val songs: List<MenuMusicData>){

    data class MenuMusicData(
        val id: Long,      //歌曲Id
        val name: String,   //歌曲名称
        @SerializedName("ar") val artists: List<StandardMusicResponse.ArtistInfo>?,
        @SerializedName("al") val album: StandardMusicResponse.AlbumInfo,
        val pop: Int?,     //热度
        val fee: Int,
        @SerializedName("dt") val duration: Int
    )

}
