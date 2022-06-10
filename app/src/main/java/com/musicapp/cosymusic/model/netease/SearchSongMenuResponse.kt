package com.musicapp.cosymusic.model.netease

/**
 * @author Eternal Epoch
 * @date 2022/6/9 23:37
 * 搜索时返回得歌单数据
 */
data class SearchSongMenuResponse(val code: Int, val result: Result){
    data class Result(val playlists: List<Playlists>)

    data class Playlists(val id: Long, val name: String, val coverImgUrl: String)
}