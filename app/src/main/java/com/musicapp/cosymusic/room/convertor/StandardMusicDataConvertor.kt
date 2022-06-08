package com.musicapp.cosymusic.room.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicapp.cosymusic.model.netease.standard.StdArtistInfo
import com.musicapp.cosymusic.model.netease.standard.StdMusicData
import java.lang.reflect.Type

/**
 * @author Eternal Epoch
 * @date 2022/6/4 20:28
 */
class StandardMusicDataConvertor {

    @TypeConverter
    fun objectToString(musicData: StdMusicData) = Gson().toJson(musicData)

    @TypeConverter
    fun stringToObject(json: String): StdMusicData {
        val songType: Type = object : TypeToken<StdMusicData>(){}.type
        return Gson().fromJson(json, songType)
    }

}

class StandardArtistDataConvertor{

    @TypeConverter
    fun objectToString(list: List<StdArtistInfo>) = Gson().toJson(list)

    @TypeConverter
    fun stringToObject(json: String): List<StdArtistInfo>{
        val listType = object : TypeToken<List<StdArtistInfo>>(){}.type
        return Gson().fromJson(json, listType)
    }
}