package com.musicapp.cosymusic.model.netease

import com.google.gson.annotations.SerializedName

/**
 * @author Eternal Epoch
 * @date 2022/6/6 21:35
 */
data class LyricResponse(@SerializedName("lrc") val lyric: LyricData?,
                         @SerializedName("tlyric") val tLyric: LyricData?,
                        val code: Int){
    data class LyricData(val lyric: String)
}
