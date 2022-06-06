package com.musicapp.cosymusic.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.musicapp.cosymusic.model.netease.StandardMusicResponse
import com.musicapp.cosymusic.model.netease.StandardMusicResponse.StandardMusicData
import com.musicapp.cosymusic.room.convertor.StandardArtistDataConvertor
import com.musicapp.cosymusic.room.convertor.StandardMusicDataConvertor

/**
 * @author Eternal Epoch
 * @date 2022/6/5 21:58
 */

@Entity
@TypeConverters(StandardMusicDataConvertor::class, StandardArtistDataConvertor::class)
data class FavoriteListData(
    @Embedded val musicData: StandardMusicData
){
    @PrimaryKey(autoGenerate = true)
    var databaseId: Long = 0
}

fun List<FavoriteListData>.toStandardList(): List<StandardMusicResponse.StandardMusicData>{
    val list = mutableListOf<StandardMusicResponse.StandardMusicData>()
    this.forEach{
        list.add(it.musicData)
    }
    return list
}