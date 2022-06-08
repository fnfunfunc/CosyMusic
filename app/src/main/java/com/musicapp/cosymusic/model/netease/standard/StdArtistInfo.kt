package com.musicapp.cosymusic.model.netease.standard

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author Eternal Epoch
 * @date 2022/6/7 21:33
 */
@Parcelize
data class StdArtistInfo(
    val name: String,
    @SerializedName("id") val artistId: Long?
): Parcelable
