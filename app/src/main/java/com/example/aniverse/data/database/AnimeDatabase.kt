package com.example.aniverse.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AnimeEntity::class, ListEntity::class, AnimeListEntity::class], version = 1)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao

    companion object {
    @Volatile
    private var INSTANCE: AnimeDatabase? = null


    fun getInstance(context: Context): AnimeDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it}
        }
    }


    private fun buildDatabase(context: Context): AnimeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AnimeDatabase::class.java,
            "anime6_db"
        ).build()

    }
    }

}