package com.example.aniverse.ui.detail

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniverse.data.api.ThemesApiModel
import com.example.aniverse.data.database.AnimeListEntity
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.data.repository.PersonalList
import com.example.aniverse.ui.list.AnimeDetailUiState
import com.example.aniverse.ui.list.ThemeUiState
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


    private val _themeList = MutableStateFlow<String>("")
    val themeList: StateFlow<String>
        get() = _themeList.asStateFlow()


    private val _listEntities = MutableStateFlow<List<PersonalList>>(emptyList())
    val listEntities: StateFlow<List<PersonalList>>
        get() = _listEntities.asStateFlow()


    private val _insertionResult = MutableStateFlow<String>("")
    val insertionResult: StateFlow<String>
        get() = _insertionResult

    fun fetch(id: Int) {
        viewModelScope.launch {
            repository.animeDetail(id).collect {
                Log.d("openingsss", it.toString())
                _uiState.value = AnimeDetailUiState(
                    it.mal_id,
                    it.title,
                    it.status,
                    it.episodes,
                    it.score,
                    it.image_url
                )
            }
        }
    }

    fun fetchOpenings(id: Int) {
        viewModelScope.launch {
            repository.getAnimeThemes(id).collect {
                val openingsAsString = it.openings.joinToString(separator = "\n") { opening ->
                    opening
                }
                _themeList.value = openingsAsString
                Log.d("THEMELIST", openingsAsString)
            }
        }
    }

    fun fetchListNames() {
        viewModelScope.launch {
                repository.lists.collect { listEntities ->
                _listEntities.value = listEntities
            }
        }
    }

    fun insertAnimeToList(listId: Int, animeId: Int) {
        viewModelScope.launch {
            try {
                repository.insertAnimeList(AnimeListEntity(listId = listId, mal_id = animeId))
                _insertionResult.value = ("El anime ha sido añadido a la lista")
            } catch (e: SQLiteConstraintException) {
                _insertionResult.value = ("El anime ya se encuentra en la lista")
            }
        }
    }

    fun resetInsertionResult() {
        _insertionResult.value = ""
    }

}