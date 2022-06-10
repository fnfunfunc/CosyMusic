package com.musicapp.cosymusic.model.netease.standard

import com.google.gson.annotations.SerializedName

/**
 * @author Eternal Epoch
 * @date 2022/6/9 16:47
 * 搜索返回的Artist信息
 */
data class StdArtistData(
    val name: String,
    @SerializedName("id") val artistId: Long?,
    val picUrl: String
)
