package com.musicapp.cosymusic.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.SongExpressResponse.SongExpressData
import com.musicapp.cosymusic.model.netease.TrackIdsResponse.MenuMusicData
import com.musicapp.cosymusic.model.netease.standard.Privilege
import com.musicapp.cosymusic.model.netease.standard.StdArtistInfo
import com.musicapp.cosymusic.model.netease.standard.StdMusicData

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
 * 从SongExpressData转为StandardMusicData
 */
fun SongExpressData.toStandard() = StdMusicData(id = id, name = name, artists = artists,
        album = album, privilege = Privilege(fee = fee, pl = null),
        pop = null, duration = duration)


/**
 * 从MenuMusicData转为StandardMusicData
 */
fun MenuMusicData.toStandard() = StdMusicData(id = id, name = name, artists = artists,
    album = album, privilege = Privilege(fee = fee, pl = null),
    duration = duration, pop = pop)

/**
 * 将artists的name转换为指定字符串
 */
fun getArtistsString(artists: List<StdArtistInfo>?): String{
    return when(val size = artists?.size ?: 0){
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

/**
 * 将dp转为px
 */
fun dp2px(dp: Float) = dp * App.context.resources.displayMetrics.density

/**
 * 数字转dp
 */
fun Int.dp() = dp2px(this.toFloat()).toInt()

/**
 * 判断RecyclerView是否滑动到底部
 * computeVerticalScrollExtent()是当前屏幕显示的区域高度，
 * computeVerticalScrollOffset() 是当前屏幕之前滑过的距离，
 * computeVerticalScrollRange()是整个View控件的高度。
 */
fun RecyclerView.isSlideToBottom() =
    (this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset()
            >= this.computeVerticalScrollRange())


/**
 * 设置颜色的透明度
 */
fun Int.setAlpha(alpha: Float): Int{
    val a = if(alpha in 0f..1f){
        Color.alpha(this) * alpha
    }else{
        255
    }.toInt()
    return Color.argb(a, Color.red(this), Color.green(this), Color.blue(this))
}