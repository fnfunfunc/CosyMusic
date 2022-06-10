package com.musicapp.cosymusic.network

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.lang.StringBuilder
import kotlin.coroutines.CoroutineContext

/**
 * @author Eternal Epoch
 * @date 2022/5/29 11:12
 */
object Repository {

    fun getSearchMusicResult(keywords: String) = fire(Dispatchers.IO){
        coroutineScope {
            val neteaseMusicResponse = NeteaseNetwork.getSearchMusicResponse(keywords)
            if(neteaseMusicResponse.code == 200){
                Result.success(neteaseMusicResponse.result)
            }else{
                Result.failure(RuntimeException("The result code of search response is " +
                        "${neteaseMusicResponse.code}"))
            }
        }
    }

    fun getSearchArtistResult(keywords: String) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getSearchArtistResponse(keywords)
            if(response.code == 200){
                Result.success(response.result)
            }else{
                Result.failure(RuntimeException("The result code of search artist is " +
                        "${response.code}"))
            }
        }
    }

    fun getSearchSongMenuResult(keywords: String) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getSearchSongMenuResponse(keywords)
            if(response.code == 200){
                Result.success(response.result)
            }else{
                Result.failure(RuntimeException("The result code of search song menu is " +
                        "${response.code}"))
            }
        }
    }

    fun getSearchAlbumResult(keywords: String) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getSearchAlbumResponse(keywords)
            if(response.code == 200){
                Result.success(response.result)
            }else{
                Result.failure(RuntimeException("The result code of search album is " +
                        "${response.code}"))
            }
        }
    }


    fun getHotSearchResult() = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getHotSearchResponse()
            if(response.code == 200){
                Result.success(response.data)
            }else{
                Result.failure(RuntimeException("获取热搜列表失败"))
            }
        }
    }

    fun getSongExpressList(type: Int) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getSongExpressList(type)
            if(response.code == 200){
                Result.success(response.data)
            }else{
                Result.failure(RuntimeException("The response of getSongExpressList body is null"))
            }
        }
    }

    fun getSearchSuggest(keywords: String) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getSearchSuggest(keywords)
            if(response.code == 200){
                Result.success(response.result)
            }else{
                Result.failure(RuntimeException("The response of getSearchSuggest is null"))
            }
        }
    }

    fun getRecommendMenu() = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getRecommendMenu()
            if(response.code == 200){
                Result.success(response.result)
            }else{
                Result.failure(RuntimeException("The response of getRecommendMenu is null"))
            }
        }
    }

    fun getSongMenuById(id: Long) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getSongMenuById(id)
            if(response.code == 200){
                Result.success(response.playlist)
            }else{
                Result.failure(RuntimeException("The response of getSongById is null"))
            }
        }
    }

    fun getMusicByTrackIds(ids: List<Long>) = fire(Dispatchers.IO){
        coroutineScope {
            val str = StringBuilder("")
            for(id in ids){
                str.append("$id,")
            }
            //以下去掉了最后面的逗号
            val response = NeteaseNetwork.getMusicByTracksId(str.slice(0..(str.length - 2)).toString())
            if(response.code == 200){
                Result.success(response.songs)
            }else{
                Result.failure(RuntimeException("The response of getMusicByTrackIds is null"))
            }
        }
    }

    fun getTopListResult() = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getTopListResponse()
            if(response.code == 200){
                Result.success(response.list)
            }else{
                Result.failure(RuntimeException("The response of getTopListResult is null"))
            }
        }
    }

    fun getLyricResult(id: Long) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getLyricResponse(id)
            if(response.code == 200){
                Result.success(response)
            }else{
                Result.failure(RuntimeException("The response of getLyricResult is null"))
            }
        }
    }

    fun getArtistInfoResult(id: Long) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getArtistInfoResponse(id)
            if(response.code == 200){
                Result.success(response.data)
            }else{
                Result.failure(RuntimeException("The response of getArtistInfoResult is null"))
            }
        }
    }

    fun getArtistSingleSongResult(id: Long) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getArtistSingleSongResponse(id)
            if(response.code == 200){
                Result.success(response.hotSongs)
            }else{
                Result.failure(RuntimeException("The response of getArtistSingleSongResult is null"))
            }
        }
    }

    fun getArtistAlbumResult(id: Long) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getArtistAlbumResponse(id)
            if(response.code == 200){
                Result.success(response.hotAlbums)
            }else{
                Result.failure(RuntimeException("The response of getArtistAlbumResult is null"))
            }
        }
    }

    fun getArtistAlbumDataResult(id: Long) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getArtistAlbumDataResponse(id)
            if(response.code == 200){
                Result.success(response)
            }else{
                Result.failure(RuntimeException("The response of getArtistAlbumDataResult is null"))
            }
        }
    }

    fun getArtistDescResult(id: Long) = fire(Dispatchers.IO){
        coroutineScope {
            val response = NeteaseNetwork.getArtistDescResponse(id)
            if(response.code == 200){
                Result.success(response)
            }else{
                Result.failure(RuntimeException("The response of getArtistDescResult is null"))
            }
        }
    }

    private fun<T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            val result = try{
                block()
            }catch (e: Exception){
                Result.failure(e)
            }
            emit(result)
        }

}