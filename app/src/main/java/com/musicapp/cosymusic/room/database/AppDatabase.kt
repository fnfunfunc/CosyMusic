package com.musicapp.cosymusic.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.musicapp.cosymusic.room.dao.PlayQueueDao
import com.musicapp.cosymusic.room.entity.PlayQueueData

/**
 * @author Eternal Epoch
 * @date 2022/6/4 20:37
 */

@Database(
    version = AppDatabase.DATABASE_VERSION,
    entities = [PlayQueueData::class],
    exportSchema = false    //不输出为Schema
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun playQueueDao(): PlayQueueDao

    companion object{
        const val DATABASE_VERSION = 1

        private var instance : AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
            AppDatabase::class.java, "app_database")
                .build().apply {
                    instance = this
                }
        }
    }

}