package com.musicapp.cosymusic.model.netease

import com.google.gson.annotations.SerializedName

/**
 * @author Eternal Epoch
 * @date 2022/6/3 20:44
 */
data class SongMenuResponse(val code: Int, val playlist: PlayList) {

    data class PlayList(val name: String, val description: String, val coverImgUrl: String,
        val trackCount: Int, val tags: List<String>?, val trackIds: List<TrackId>)

    data class TrackId(val id: Long)

}
