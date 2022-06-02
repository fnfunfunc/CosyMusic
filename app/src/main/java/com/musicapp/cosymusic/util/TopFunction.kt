package com.musicapp.cosymusic.util

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.SongExpressResponse.SongExpressData
import com.musicapp.cosymusic.model.netease.StandardMusicResponse
import com.musicapp.cosymusic.model.netease.StandardMusicResponse.StandardMusicData

/**
 * @author Eternal Epoch
 * @date 2022/5/29 17:40
 */

/**
 * Toast消息
 */
fun toast(text: String){
    Toast.makeText(App.context, text, Toast.LENGTH_SHORT).show()
}

/**
 * 运行在主线程中
 */
fun runOnMainThread(runnable: Runnable){
    Handler(Looper.getMainLooper()).post(runnable)
}

/**
 * Glide取消缓存
 */
fun RequestBuilder<Drawable>.cancelCache(): RequestBuilder<Drawable>{
    //分别取消了内存缓存和磁盘缓存
    return skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
}

/**
 * 从SongExpressResponse转为StandardMusicData
 */
fun SongExpressData.toStandard(): StandardMusicData{
    return StandardMusicData(id = id, name = name, artists = artists,
        album = album, privilege = StandardMusicResponse.Privilege(fee = fee, pl = null),
        pop = null, duration = duration)
}

/**
 * 将artists的name转换为指定字符串
 */
fun getArtistsString(artists: List<StandardMusicResponse.ArtistInfo>?): String{
    val size = artists?.size ?: 0
    return when(size){
        0 -> "未知"
        1 -> artists!![0].name
        else -> {
            val res = StringBuilder()
            for(i in 0 until size - 1){
                res.append("${artists!![i].name} / ")
            }
            res.append(artists!![size - 1].name)
            res.toString()
        }
    }
}