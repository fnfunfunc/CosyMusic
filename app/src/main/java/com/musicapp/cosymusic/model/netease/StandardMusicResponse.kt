package com.musicapp.cosymusic.model.netease

import android.os.Parcelable
import com.musicapp.cosymusic.model.netease.standard.StdMusicData
import kotlinx.parcelize.Parcelize

/**
 * @author Eternal Epoch
 * @date 2022/5/29 10:16
 */
@Parcelize
data class StandardMusicResponse(val result: Result, val code: Int): Parcelable{
    @Parcelize
    data class Result(val songs: List<StdMusicData>, val songCount: Int): Parcelable

}
