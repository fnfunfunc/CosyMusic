package com.musicapp.cosymusic.model.netease.standard

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author Eternal Epoch
 * @date 2022/6/7 21:32
 */
@Parcelize
data class StdMusicData(
    val id: Long,      //歌曲Id
    val name: String,   //歌曲名称
    @SerializedName("ar") val artists: List<StdArtistInfo>?,
    @Embedded @SerializedName("al") val album: StdAlbumInfo,
    val pop: Int?,     //热度
    @Embedded val privilege: Privilege,
    @SerializedName("dt") val duration: Int
): Parcelable
