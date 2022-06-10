package com.musicapp.cosymusic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author Eternal Epoch
 * @date 2022/5/29 11:06
 */
object NeteaseNetwork {

    private val neteaseSearchService = ServiceCreator.create<NeteaseService>()

    suspend fun getSearchMusicResponse(keywords: String) =
        neteaseSearchService.getSearchMusicResponse(keywords).await()

    suspend fun getSearchArtistResponse(keywords: String) =
        neteaseSearchService.getSearchArtistResponse(keywords).await()

    suspend fun getSearchSongMenuResponse(keywords: String) =
        neteaseSearchService.getSearchSongMenuResponse(keywords).await()

    suspend fun getSearchAlbumResponse(keywords: String) =
        neteaseSearchService.getSearchAlbumResponse(keywords).await()

    suspend fun getHotSearchResponse() =
        neteaseSearchService.getHotSearchResponse().await()

    suspend fun getSearchSuggest(keywords: String) =
        neteaseSearchService.getSearchSuggest(keywords).await()

    suspend fun getSongExpressList(type: Int) = neteaseSearchService.getSongExpressList(type).await()

    suspend fun getRecommendMenu() = neteaseSearchService.getRecommendMenu().await()

    suspend fun getSongMenuById(id: Long) = neteaseSearchService.getSongMenuById(id).await()

    suspend fun getMusicByTracksId(ids: String) = neteaseSearchService.getMusicByTracksId(ids).await()

    suspend fun getTopListResponse() = neteaseSearchService.getTopListResponse().await()

    suspend fun getLyricResponse(id: Long) = neteaseSearchService.getLyricResponse(id).await()

    suspend fun getArtistInfoResponse(id: Long) = neteaseSearchService.getArtistInfoResponse(id).await()

    suspend fun getArtistSingleSongResponse(id: Long) = neteaseSearchService.getArtistSingleSongResponse(id).await()

    suspend fun getArtistAlbumResponse(id: Long) = neteaseSearchService.getArtistAlbumResponse(id).await()

    suspend fun getArtistAlbumDataResponse(id: Long) = neteaseSearchService.getArtistAlbumDataResponse(id).await()

    suspend fun getArtistDescResponse(id: Long) = neteaseSearchService.getArtistDescResponse(id).await()

    private suspend fun<T> Call<T>.await(): T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null){
                        continuation.resume(body)
                    }else{
                        continuation.resumeWithException(RuntimeException("Response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}