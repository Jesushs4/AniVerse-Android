package com.example.aniverse.ui.characterList

import com.example.aniverse.data.repository.Anime
import com.example.aniverse.data.repository.AnimeCharacter

data class CharacterListUiState(
    val character: List<AnimeCharacter>,
    val errorMessage: String?=null
)