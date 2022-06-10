package com.musicapp.cosymusic.model.netease

import com.musicapp.cosymusic.model.netease.standard.StdAlbumDetailInfo

/**
 * @author Eternal Epoch
 * @date 2022/6/10 12:59
 */
data class SearchAlbumResponse(val code: Int, val result: Result){

    data class Result(val albums: List<StdAlbumDetailInfo>)

}
