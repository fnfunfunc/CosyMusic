package com.musicapp.cosymusic.model.netease.standard

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Eternal Epoch
 * @date 2022/6/7 21:34
 */
@Parcelize
data class Privilege(
    val fee: Int, //是否为网易云Vip歌曲，1代表是
    val pl: Int?, //网易云是否有版权，0代表无
    //@SerializedName("maxbr") val maxBr: Int, //最高音质
): Parcelable