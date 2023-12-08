package com.example.aniverse.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listAnimeEntity: List<AnimeEntity>)

    @Query("SELECT * FROM anime")
    fun getAll(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE mal_id= :id")
    fun getAnime(id:Int): Flow<AnimeEntity>
}