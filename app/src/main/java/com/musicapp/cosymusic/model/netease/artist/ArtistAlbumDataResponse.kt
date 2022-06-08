package com.musicapp.cosymusic.model.netease.artist

import com.musicapp.cosymusic.model.netease.standard.StdAlbumData
import com.musicapp.cosymusic.model.netease.standard.StdMusicData

/**
 * @author Eternal Epoch
 * @date 2022/6/8 8:57
 */
data class ArtistAlbumDataResponse(val code: Int, val songs: List<StdMusicData>, val album: StdAlbumData)
