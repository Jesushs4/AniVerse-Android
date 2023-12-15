package com.example.aniverse.ui.animeLists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniverse.data.database.ListEntity
import com.example.aniverse.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    private val _operationResult = MutableStateFlow<String>("")
    val operationResult: StateFlow<String>
        get() = _operationResult

    init {
        viewModelScope.launch {
            repository.lists.collect {
                _uiState.value = PersonalListUiState(it)
            }
        }
    }

    fun createList(name: String) {
        val newList = ListEntity(name = name)
        viewModelScope.launch {
            repository.insertList(newList)
            _operationResult.value = ("Se ha creado la lista")
        }
    }
    fun editListName(listId:Int, name: String) {
        viewModelScope.launch {
            repository.updateListName(listId, name)
            _operationResult.value = ("Se ha cambiado el nombre de la lista")
        }
    }
    fun deleteList(listId:Int) {
        viewModelScope.launch {
            repository.deleteList(listId)
            _operationResult.value = ("Se ha borrado la lista")
        }
    }

    fun resetOperationResult() {
        _operationResult.value = ""
    }

}