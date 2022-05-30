package com.musicapp.cosymusic.model.netease

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

/**
 * @author Eternal Epoch
 * @date 2022/5/29 10:16
 */
data class MusicResponse(val result: Result, val code: Int){
    data class Result(val songs: List<MusicData>, val songCount: Int)

    data class MusicData(
        val id: Long,      //歌曲Id
        val name: String,   //歌曲名称
        @SerializedName("ar") val artist: List<ArtistInfo>?,
        @SerializedName("al") val album: AlbumInfo,
        val pop: Int,     //热度
        val privilege: Privilege,
        @SerializedName("dt") val duration: Int
    )

    data class ArtistInfo(
        @SerializedName("id") val artistId: Long?,
        val name: String?
    )

    data class AlbumInfo(val picUrl: String)

    data class Privilege(
        val fee: Int, //是否为网易云Vip歌曲，1代表是
        val pl: Int, //网易云是否有版权，0代表无
        @SerializedName("maxbr") val maxBr: Int, //最高音质
    )
}
