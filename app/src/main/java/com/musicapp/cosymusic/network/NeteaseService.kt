package com.musicapp.cosymusic.network

import com.musicapp.cosymusic.model.netease.HotSearchResponse
import com.musicapp.cosymusic.model.netease.SearchSuggestResponse
import com.musicapp.cosymusic.model.netease.SongExpressResponse
import com.musicapp.cosymusic.model.netease.StandardMusicResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Eternal Epoch
 * @date 2022/5/29 11:04
 */

//网易云Service
interface NeteaseService {

    @GET("cloudsearch")
    fun getSearchResponse(@Query("keywords") keywords: String): Call<StandardMusicResponse>


    @GET("search/hot/detail")
    fun getHotSearchResponse(): Call<HotSearchResponse>

    @GET("top/song")
    fun getSongExpressList(@Query("type") type: Int): Call<SongExpressResponse>

    @GET("search/suggest?type=mobile")
    fun getSearchSuggest(@Query("keywords") keywords: String): Call<SearchSuggestResponse>
}