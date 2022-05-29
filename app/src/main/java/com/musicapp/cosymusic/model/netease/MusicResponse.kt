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
        @Embedded
        val neteaseInfo: NeteaseInfo
    )

    data class ArtistInfo(
        @SerializedName("id") val artistId: Long?,
        val name: String?
    )

    data class AlbumInfo(val picUrl: String)

    data class NeteaseInfo(
        val fee: Int,   //是否为网易云Vip歌曲，1代表是
        val pl: Int,    //若为0，则为无效歌曲，代表网易云无版权
        @SerializedName("maxbr") val maxBr: Int,    //最大音质

    )
}
