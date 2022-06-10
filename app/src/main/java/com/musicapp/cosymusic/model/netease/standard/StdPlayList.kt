package com.musicapp.cosymusic.model.netease.standard


/**
 * @author Eternal Epoch
 * @date 2022/6/9 23:08
 */
data class StdPlayList(val name: String, val description: String, val coverImgUrl: String,
                       val trackCount: Int, val tags: List<String>?,
                       val trackIds: List<TrackId>){
    data class TrackId(val id: Long)
}
