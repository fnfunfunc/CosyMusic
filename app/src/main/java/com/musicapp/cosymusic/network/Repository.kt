package com.musicapp.cosymusic.network

import androidx.lifecycle.liveData
import com.musicapp.cosymusic.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * @author Eternal Epoch
 * @date 2022/5/29 11:12
 */
object Repository {

    fun getSearchResponse(keywords: String) = fire(Dispatchers.IO){
        coroutineScope {
            val neteaseMusicResponse = NeteaseNetwork.getSearchResponse(keywords)
            if(neteaseMusicResponse.code == 200){
                Result.success(neteaseMusicResponse.result)
            }else{
                Result.failure(RuntimeException("The result code of search response is " +
                        "${neteaseMusicResponse.code}"))
            }
        }
    }


    fun getHotSearchResponse() = fire(Dispatchers.IO){
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