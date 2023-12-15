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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(listListEntity: ListEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAnimeList(animeListEntity: AnimeListEntity)

    @Query("SELECT * FROM anime")
    fun getAll(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE mal_id= :id")
    fun getAnime(id:Int): Flow<AnimeEntity>

    @Query("SELECT * FROM lists")
    fun getAllList(): Flow<List<ListEntity>>

    @Query("SELECT * FROM lists WHERE listId= :id")
    fun getAnimeList(id:Int): Flow<ListWithAnimes>

    @Query("DELETE FROM lists WHERE listId = :id")
    suspend fun deleteList(id: Int)

    @Query("UPDATE lists SET name = :newName WHERE listId = :id")
    suspend fun updateListName(id: Int, newName: String)

    @Query("DELETE FROM animelist WHERE mal_id= :id")
    suspend fun deleteAnimeFromList(id:Int)

    /*@Query("UPDATE anime SET user_score = :newScore WHERE mal_id = :id")
    suspend fun updateUserScore(id: Int, newScore: Int)*/
}