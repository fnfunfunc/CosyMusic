package com.musicapp.cosymusic.model.netease.standard

/**
 * @author Eternal Epoch
 * @date 2022/6/7 21:55
 */

/**
 * 专辑列表获取到的信息
 */
data class StdAlbumDetailInfo(val id: Long, val name: String,
                              val onSale: Boolean, val picUrl: String)