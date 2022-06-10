package com.musicapp.cosymusic.model.netease

import com.google.gson.annotations.SerializedName
import com.musicapp.cosymusic.model.netease.standard.StdPlayList

/**
 * @author Eternal Epoch
 * @date 2022/6/3 20:44
 */
data class RecommendSongMenuResponse(val code: Int, val playlist: StdPlayList)
