package com.musicapp.cosymusic.model.netease

/**
 * @author Eternal Epoch
 * @date 2022/6/2 16:44
 */
data class TopListResponse(val code: Int, val list: List<TopList>){

    data class TopList(val id: Long,val name: String, val coverImgUrl: String, val updateFrequency: String)

}
