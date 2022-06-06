package com.musicapp.cosymusic.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.musicapp.cosymusic.room.dao.FavoriteListDao
import com.musicapp.cosymusic.room.dao.PlayQueueDao
import com.musicapp.cosymusic.room.entity.FavoriteListData
import com.musicapp.cosymusic.room.entity.PlayQueueData

/**
 * @author Eternal Epoch
 * @date 2022/6/4 20:37
 */

@Database(
    version = AppDatabase.DATABASE_VERSION,
    entities = [PlayQueueData::class, FavoriteListData::class],
    exportSchema = false    //不输出为Schema
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun playQueueDao(): PlayQueueDao

    abstract fun favoriteListDao(): FavoriteListDao

    companion object{
        const val DATABASE_VERSION = 1

        private var instance : AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""create table FavoriteListData (
                    |id integer not null,
                    |name text,
                    |artists text,
                    |picUrl text,
                    |pop integer,
                    |fee integer,
                    |pl integer,
                    |duration integer
                    |)""".trimMargin())
            }
        }

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