package com.musicapp.cosymusic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Eternal Epoch
 * @date 2022/5/29 11:01
 */
object ServiceCreator {

    private const val BASE_URL = "http://www.orientsky.xyz/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun<T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun<reified T> create() = create(T::class.java)
}