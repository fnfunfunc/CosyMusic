package com.musicapp.cosymusic.room.dao

import androidx.room.*
import com.musicapp.cosymusic.room.entity.FavoriteListData

/**
 * @author Eternal Epoch
 * @date 2022/6/5 22:01
 */
@Dao
interface FavoriteListDao {

    @Insert
    fun insert(musicData: FavoriteListData): Long

    @Update
    fun update(musicData: FavoriteListData)

    @Query("select * from FavoriteListData")
    fun loadAll(): List<FavoriteListData>

    @Delete
    fun delete(musicData: FavoriteListData)

    @Query("delete from FavoriteListData where id = :id")
    fun deleteById(id: Long)

    @Query("delete from FavoriteListData")
    fun clear()
}