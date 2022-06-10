package com.musicapp.cosymusic.model.netease

import com.musicapp.cosymusic.model.netease.standard.StdArtistData

/**
 * @author Eternal Epoch
 * @date 2022/6/9 16:51
 */
data class SearchArtistResponse(val code: Int, val result: SearchResult){

    data class SearchResult(val artistCount: Int, val artists: List<StdArtistData>)

}
