package com.example.aniverse.ui.listDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniverse.data.database.AnimeDBRepository
import com.example.aniverse.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ListDetailViewModel @Inject constructor(private val repository: AnimeRepository): ViewModel() {

    private val _uiState = MutableStateFlow(ListDetailUiState(listOf()))
    val uiState: StateFlow<ListDetailUiState>
        get()=_uiState.asStateFlow()

    fun fetchAnimeListByListId(listId: Int) {
        viewModelScope.launch {
            try {
                repository.getAnimeList(listId).collect { animeListEntities ->
                    val animeEntities = animeListEntities.map {
                        repository.animeDetail(it.animeId).first()
                    }
                    _uiState.value = ListDetailUiState(animeEntities)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Unknown error")
            }
        }
    }
}