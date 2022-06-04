package com.musicapp.cosymusic.model.netease

/**
 * @author Eternal Epoch
 * @date 2022/6/2 19:32
 */
data class RecommendMenuResponse(val code: Int, val result: List<Result>){

    data class Result(val id: Long, val name: String, val picUrl: String, val playCount: Int)
}
