package com.example.aniverse.data.api

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeApiRepository @Inject constructor(private val service: AnimeService) {
    suspend fun getAll():List<AnimeApiModel> {
        val list = service.api.getAll("popularity", 5,20, 0)
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

    suspend fun getAllCharacters(): List<CharacterApiModel> {
        val list = service.api.getAllCharacters("favorites", "desc", 20, 0)
        val characterApiModel = list.data.map {
            character -> CharacterApiModel(
                character.mal_id,
                character.name,
                character.images.jpg.image_url
            )
        }
        return characterApiModel
    }

    suspend fun getCharacters(query:String): List<CharacterApiModel> {
        val list = service.api.getCharacters("favorites", query, "desc", 20, 0)
        val characterApiModel = list.data.map {
                character -> CharacterApiModel(
            character.mal_id,
            character.name,
            character.images.jpg.image_url
        )
        }
        return characterApiModel
    }

    suspend fun getAnimeThemes(id: Int):ThemesApiModel {
        val themes = service.api.getAnimeThemes(id)
        val newThemes =
        ThemesApiModel(
            themes.data.openings,
            themes.data.endings)

        return newThemes
    }
}