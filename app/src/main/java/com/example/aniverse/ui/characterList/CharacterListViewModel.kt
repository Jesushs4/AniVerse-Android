package com.example.aniverse.ui.characterList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniverse.data.repository.AnimeCharacter
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.ui.list.AnimeListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(private val repository: AnimeRepository):
    ViewModel() {
    private val _uiState = MutableStateFlow(CharacterListUiState(listOf()))
    val uiState: StateFlow<CharacterListUiState>
        get() = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            repository.getAllCharacters().collect {
                var characters = it.map {
                    AnimeCharacter(
                        it.mal_id,
                        it.name,
                        it.images
                    )
                }
                _uiState.value = CharacterListUiState(characters)
            }
        }
    }

    fun searchCharacters(query: String) {
        viewModelScope.launch {
            repository.getCharacters(query).collect {
                var characters = it.map {
                    AnimeCharacter(
                        it.mal_id,
                        it.name,
                        it.images
                    )
                }
                _uiState.value = CharacterListUiState(characters)
            }
        }
    }
}