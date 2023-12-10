package com.example.aniverse.data.database

import androidx.annotation.WorkerThread
import com.example.aniverse.data.repository.Anime
import com.example.aniverse.data.repository.AnimeList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeDBRepository @Inject constructor(private val animeDao: AnimeDao){

    val allAnime: Flow<List<AnimeEntity>> = animeDao.getAll()

    val allList: Flow<List<ListEntity>> = animeDao.getAllList()

    @WorkerThread
    suspend fun animeDetail(id:Int): Flow<Anime> {
        return animeDao.getAnime(id).map { it.asAnime() }
    }

    @WorkerThread
    suspend fun insert(listAnimeEntity: List<AnimeEntity>) {
        animeDao.insert(listAnimeEntity)
    }

    @WorkerThread
    suspend fun insertList(listEntity: ListEntity) {
        animeDao.insertList(listEntity)
    }

    @WorkerThread
    suspend fun insertAnimeList(animeListEntity: AnimeListEntity) {
        animeDao.insertAnimeList(animeListEntity)
    }

    @WorkerThread
    suspend fun getAnimeList(id: Int): Flow<List<AnimeListEntity>> {
        return animeDao.getAnimeList(id)
    }

    @WorkerThread
    suspend fun deleteList(id: Int) {
        animeDao.deleteList(id)
    }

    @WorkerThread
    suspend fun updateListName(id:Int, name:String) = animeDao.updateListName(id, name)
}