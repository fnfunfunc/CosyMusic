package com.musicapp.cosymusic.model.netease

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author Eternal Epoch
 * @date 2022/5/29 10:16
 */
@Parcelize
data class StandardMusicResponse(val result: Result, val code: Int): Parcelable{
    @Parcelize
    data class Result(val songs: List<StandardMusicData>, val songCount: Int): Parcelable

    @Parcelize
    data class StandardMusicData(
        val id: Long,      //歌曲Id
        val name: String,   //歌曲名称
        @SerializedName("ar") val artists: List<ArtistInfo>?,
        @Embedded @SerializedName("al") val album: AlbumInfo,
        val pop: Int?,     //热度
        @Embedded val privilege: Privilege,
        @SerializedName("dt") val duration: Int
    ): Parcelable

    @Parcelize
    data class ArtistInfo(
        val name: String
    ): Parcelable

    @Parcelize
    data class AlbumInfo(val picUrl: String): Parcelable

    @Parcelize
    data class Privilege(
        val fee: Int, //是否为网易云Vip歌曲，1代表是
        val pl: Int?, //网易云是否有版权，0代表无
        //@SerializedName("maxbr") val maxBr: Int, //最高音质
    ): Parcelable
}
