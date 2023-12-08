package com.example.aniverse.data.repository

import android.util.Log
import com.example.aniverse.data.api.AnimeApiRepository
import com.example.aniverse.data.api.asEntityModel
import com.example.aniverse.data.database.AnimeDBRepository
import com.example.aniverse.data.database.asAnime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AnimeRepository @Inject constructor(
    private val dbRepository: AnimeDBRepository,
    private val apiRepository: AnimeApiRepository
) {
    val anime: Flow<List<Anime>>
        get() {
            val list = dbRepository.allAnime.map {
                it.asAnime()
            }
            return list
        }

    suspend fun refreshList() {
        withContext(Dispatchers.IO) {
            val apiAnime = apiRepository.getAll()
            dbRepository.insert(apiAnime.asEntityModel())
        }
    }

    suspend fun animeDetail(id:Int) = dbRepository.animeDetail(id)
}