package com.musicapp.cosymusic.model.netease.standard

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author Eternal Epoch
 * @date 2022/6/7 21:34
 */

/**
 * 搜索歌曲返回歌曲所在的专辑信息
 */
@Parcelize
data class StdAlbumInfo(val picUrl: String, @SerializedName("id") val albumId: Long): Parcelable
