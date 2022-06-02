package com.musicapp.cosymusic.model.netease

/**
 * @author Eternal Epoch
 * @date 2022/6/2 13:58
 */
data class SearchSuggestResponse(val code: Int, val result: Result ){

    //这里记得判空，因为可能并没有任何匹配的推荐信息
    data class Result(val allMatch: List<Match>?)

    data class Match(val keyword: String)

}
