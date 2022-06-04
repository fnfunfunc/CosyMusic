package com.musicapp.cosymusic.room.dao

import androidx.room.*
import com.musicapp.cosymusic.room.entity.PlayQueueData

/**
 * @author Eternal Epoch
 * @date 2022/6/4 20:22
 */

@Dao
interface PlayQueueDao {

    @Insert
    fun insert(playQueueData: PlayQueueData): Long

    @Update
    fun update(playQueueData: PlayQueueData)

    @Query("select * from PlayQueueData")
    fun loadAll(): List<PlayQueueData>

    @Delete
    fun delete(playQueueData: PlayQueueData)

    @Query("delete from PlayQueueData where id = :id")
    fun deleteById(id: Long): Int

    @Query("delete from PlayQueueData")
    fun clear()
}