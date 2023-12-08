package com.example.aniverse.data.api

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeApiRepository @Inject constructor(private val service: AnimeService) {
    suspend fun getAll():List<AnimeApiModel> {
        val list = service.api.getAll(20, 0)
        val animeApiModel = list.data.map {
            anime -> AnimeApiModel(
                anime.mal_id,
                anime.title,
                anime.status,
                anime.episodes,
                anime.score,
                anime.images.jpg.image_url
            )
        }
        return animeApiModel
    }
}