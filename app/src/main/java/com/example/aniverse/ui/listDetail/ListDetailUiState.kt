package com.example.aniverse.ui.listDetail

import com.example.aniverse.data.repository.Anime

data class ListDetailUiState(
    val anime: List<Anime>,
    val errorMessage: String?=null
)
