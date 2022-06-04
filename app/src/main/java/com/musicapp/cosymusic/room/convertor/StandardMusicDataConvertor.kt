package com.musicapp.cosymusic.room.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicapp.cosymusic.model.netease.StandardMusicResponse.ArtistInfo
import com.musicapp.cosymusic.model.netease.StandardMusicResponse.StandardMusicData
import java.lang.reflect.Type

/**
 * @author Eternal Epoch
 * @date 2022/6/4 20:28
 */
class StandardMusicDataConvertor {

    @TypeConverter
    fun objectToString(musicData: StandardMusicData) = Gson().toJson(musicData)

    @TypeConverter
    fun stringToObject(json: String): StandardMusicData {
        val songType: Type = object : TypeToken<StandardMusicData>(){}.type
        return Gson().fromJson(json, songType)
    }

}

class StandardArtistDataConvertor{

    @TypeConverter
    fun objectToString(list: List<ArtistInfo>) = Gson().toJson(list)

    @TypeConverter
    fun stringToObject(json: String): List<ArtistInfo>{
        val listType = object : TypeToken<List<ArtistInfo>>(){}.type
        return Gson().fromJson(json, listType)
    }
}