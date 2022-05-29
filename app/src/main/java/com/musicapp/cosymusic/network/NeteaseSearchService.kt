package com.musicapp.cosymusic.network

import com.musicapp.cosymusic.model.netease.MusicSource
import com.musicapp.cosymusic.model.netease.MusicResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Eternal Epoch
 * @date 2022/5/29 11:04
 */

//网易云搜索歌曲Service
interface NeteaseSearchService {

    @GET("cloudsearch")
    fun getSearchResponse(@Query("keywords") keywords: String): Call<MusicResponse>

    @GET("song/url")
    fun getMusicSourceById(@Query("id") id: Long): Call<MusicSource>

}