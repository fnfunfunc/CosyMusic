package com.musicapp.cosymusic.model.netease

/**
 * @author Eternal Epoch
 * @date 2022/5/29 17:06
 */
data class MusicSource(val data: List<Music>, val code: Int) {

    data class Music(val url: String)
}