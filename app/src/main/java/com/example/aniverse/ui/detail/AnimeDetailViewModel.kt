package com.example.aniverse.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.data.repository.PersonalList
import com.example.aniverse.ui.list.AnimeDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(private val repository: AnimeRepository): ViewModel() {

    private val _uiState = MutableStateFlow(AnimeDetailUiState())
    val uiState: StateFlow<AnimeDetailUiState>
        get() = _uiState.asStateFlow()

    private val _listEntities = MutableStateFlow<List<PersonalList>>(emptyList())
    val listEntities: StateFlow<List<PersonalList>>
        get() = _listEntities.asStateFlow()

    fun fetch(id: Int) {
        viewModelScope.launch {
            repository.animeDetail(id).collect {
                _uiState.value = AnimeDetailUiState(
                    it.mal_id,
                    it.title,
                    it.status,
                    it.episodes,
                    it.score,
                    it.image_url
                )
            }
            fetchListNames()
        }
    }

    fun fetchListNames() {
        viewModelScope.launch {
            val lists = repository.lists.collect { listEntities ->
                _listEntities.value = listEntities
            }
        }
    }
}