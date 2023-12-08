package com.example.aniverse.ui.list

import com.example.aniverse.data.repository.Anime

data class AnimeListUiState(
    val anime: List<Anime>,
    val errorMessage: String?=null
)
