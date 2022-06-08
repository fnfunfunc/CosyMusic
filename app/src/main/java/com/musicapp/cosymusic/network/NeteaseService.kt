package com.musicapp.cosymusic.network

import com.musicapp.cosymusic.model.netease.*
import com.musicapp.cosymusic.model.netease.artist.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Eternal Epoch
 * @date 2022/5/29 11:04
 */

//网易云Service
interface NeteaseService {

    //搜索结果
    @GET("cloudsearch")
    fun getSearchResponse(@Query("keywords") keywords: String): Call<StandardMusicResponse>

    //热搜列表
    @GET("search/hot/detail")
    fun getHotSearchResponse(): Call<HotSearchResponse>

    //新歌速递
    @GET("top/song")
    fun getSongExpressList(@Query("type") type: Int): Call<SongExpressResponse>

    //搜索建议
    @GET("search/suggest?type=mobile")
    fun getSearchSuggest(@Query("keywords") keywords: String): Call<SearchSuggestResponse>

    //歌单推荐
    @GET("personalized")
    fun getRecommendMenu(): Call<RecommendMenuResponse>

    //歌单详情
    @GET("playlist/detail")
    fun getSongMenuById(@Query("id") id: Long): Call<SongMenuResponse>

    //歌曲详情
    @GET("song/detail")
    fun getMusicByTracksId(@Query("ids") ids: String): Call<TrackIdsResponse>

    //排行榜
    @GET("toplist")
    fun getTopListResponse(): Call<TopListResponse>

    //歌词
    @GET("lyric")
    fun getLyricResponse(@Query("id") id: Long): Call<LyricResponse>

    //Artist详情
    @GET("artist/detail")
    fun getArtistInfoResponse(@Query("id") id: Long): Call<ArtistInfoResponse>

    //Artist单曲
    @GET("artists")
    fun getArtistSingleSongResponse(@Query("id") id: Long): Call<ArtistSingleSongResponse>

    //Artist专辑 传入Artist的id
    @GET("artist/album")
    fun getArtistAlbumResponse(@Query("id") id: Long): Call<ArtistAlbumInfoResponse>

    //获取专辑包含的曲目，传入Album的id
    @GET("album")
    fun getArtistAlbumDataResponse(@Query("id") id: Long): Call<ArtistAlbumDataResponse>

    //获取Artist描述，传入Artist的id
    @GET("artist/desc")
    fun getArtistDescResponse(@Query("id") id: Long): Call<ArtistDescResponse>
}