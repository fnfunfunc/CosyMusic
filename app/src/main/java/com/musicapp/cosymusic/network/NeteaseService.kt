package com.musicapp.cosymusic.network

import com.musicapp.cosymusic.model.netease.*
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

    @GET("personalized")
    fun getRecommendMenu(): Call<RecommendMenuResponse>

    @GET("playlist/detail")
    fun getSongMenuById(@Query("id") id: Long): Call<SongMenuResponse>

    @GET("song/detail")
    fun getMusicByTracksId(@Query("ids") ids: String): Call<TrackIdsResponse>
}