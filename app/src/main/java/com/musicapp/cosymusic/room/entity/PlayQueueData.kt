package com.musicapp.cosymusic.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.musicapp.cosymusic.model.netease.standard.StdMusicData
import com.musicapp.cosymusic.room.convertor.StandardArtistDataConvertor
import com.musicapp.cosymusic.room.convertor.StandardMusicDataConvertor

/**
 * @author Eternal Epoch
 * @date 2022/6/4 20:08
 */

@Entity
@TypeConverters(StandardMusicDataConvertor::class, StandardArtistDataConvertor::class)
data class PlayQueueData(
    @Embedded
    val songData: StdMusicData
){
    @PrimaryKey(autoGenerate = true)
    var databaseId: Long = 0
}

fun List<PlayQueueData>.toStandardList(): List<StdMusicData>{
    val list = mutableListOf<StdMusicData>()
    this.forEach{
        list.add(it.songData)
    }
    return list
}
