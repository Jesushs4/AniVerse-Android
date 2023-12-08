package com.example.aniverse.ui.animeLists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniverse.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PersonalListViewModel @Inject constructor(private val repository: AnimeRepository): ViewModel() {

    private val _uiState = MutableStateFlow(PersonalListUiState(listOf()))
    val uiState: StateFlow<PersonalListUiState>
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
            repository.lists.collect {
                _uiState.value = PersonalListUiState(it)
            }
        }
    }
}