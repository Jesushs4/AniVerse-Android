package com.example.aniverse.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniverse.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(private val repository: AnimeRepository): ViewModel() {

    private val _uiState = MutableStateFlow(AnimeListUiState(listOf()))
    val uiState: StateFlow<AnimeListUiState>
        get()=_uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                repository.refreshList()
            } catch (e:IOException) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message!!)
            }
        }
        viewModelScope.launch {
            repository.anime.collect {
                _uiState.value = AnimeListUiState(it)
            }
        }
    }
}