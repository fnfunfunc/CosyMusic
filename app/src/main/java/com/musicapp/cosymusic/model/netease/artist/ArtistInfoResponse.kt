package com.musicapp.cosymusic.model.netease.artist

/**
 * @author Eternal Epoch
 * @date 2022/6/7 14:40
 */
data class ArtistInfoResponse(val code: Int, val data: ArtistInfoData){

    data class ArtistInfoData(val artist: ArtistInfoDetail)

    data class ArtistInfoDetail(val cover: String, val name: String, val transNames: List<String>,
                 val briefDesc: String?, val albumSize: Int, val musicSize: Int, val mvSize: Int)

}
