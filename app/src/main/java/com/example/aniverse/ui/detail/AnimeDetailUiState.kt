package com.example.aniverse.ui.list

import com.example.aniverse.data.repository.Anime

data class AnimeDetailUiState(
    val mal_id: Int = 0,
    val title: String = "",
    val status: String = "",
    val episodes: Int = 0,
    val score: Double = 0.0,
    val image_url: String = ""
)
