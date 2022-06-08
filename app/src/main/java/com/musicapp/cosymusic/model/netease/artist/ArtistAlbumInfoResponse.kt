package com.musicapp.cosymusic.model.netease.artist

import com.musicapp.cosymusic.model.netease.standard.StdAlbumDetailInfo

/**
 * @author Eternal Epoch
 * @date 2022/6/7 21:29
 */
data class ArtistAlbumInfoResponse(val code: Int, val hotAlbums: List<StdAlbumDetailInfo>)
