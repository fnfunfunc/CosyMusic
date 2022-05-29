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

    private val neteaseSearchService = ServiceCreator.create<NeteaseSearchService>()

    suspend fun getSearchResponse(keywords: String) =
        neteaseSearchService.getSearchResponse(keywords).await()

    suspend fun getMusicSourceById(id: Long) =
        neteaseSearchService.getMusicSourceById(id).await()

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