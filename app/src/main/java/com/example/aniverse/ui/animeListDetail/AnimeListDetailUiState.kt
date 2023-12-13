package com.example.aniverse.ui.animeListDetail

data class AnimeListDetailUiState(
    val mal_id: Int = 0,
    val title: String = "",
    val status: String = "",
    val episodes: Int = 0,
    val score: Double = 0.0,
    val image_url: String = ""
)
