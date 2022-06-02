package com.musicapp.cosymusic.model.netease

/**
 * @author Eternal Epoch
 * @date 2022/6/1 17:05
 */
data class HotSearchResponse(val code: Int, val data: List<Data>) {

    data class Data(val searchWord: String, val score: Int, val iconUrl: String?)
}