package com.musicapp.cosymusic.model.netease.artist

import com.google.gson.annotations.SerializedName

/**
 * @author Eternal Epoch
 * @date 2022/6/8 13:47
 */
data class ArtistDescResponse(val code: Int, val briefDesc: String, val introduction: List<Introduction>){

    data class Introduction(@SerializedName("ti") val title: String, @SerializedName("txt") val content: String)

}
