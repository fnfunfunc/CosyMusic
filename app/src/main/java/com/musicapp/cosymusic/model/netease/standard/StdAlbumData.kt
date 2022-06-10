package com.musicapp.cosymusic.model.netease.standard

/**
 * @author Eternal Epoch
 * @date 2022/6/8 8:55
 */

data class StdAlbumData(
    val id: Long, val name: String, val picUrl: String,
    val description: String, val publishTime: Long,
    val artistData: List<StdArtistInfo>
)
