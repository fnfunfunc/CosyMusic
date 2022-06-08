package com.musicapp.cosymusic.model.netease.artist

import com.musicapp.cosymusic.model.netease.standard.StdMusicData

/**
 * @author Eternal Epoch
 * @date 2022/6/7 19:24
 */
data class ArtistSingleSongResponse(val code: Int, val hotSongs: List<StdMusicData>)
