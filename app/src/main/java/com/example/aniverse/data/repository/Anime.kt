package com.example.aniverse.data.repository

data class Anime(
    val mal_id: Int,
    val title: String,
    val status: String,
    val episodes: Int,
    val score: Double,
    val image_url: String
)

data class AnimeCharacter(
    val mal_id: Int,
    val name: String,
    val image_url: String
)

data class PersonalList(
    val id: Int,
    val name: String
)

data class AnimeList(
    val id: Int,
    val listId: Int,
    val animeId: Int
)
