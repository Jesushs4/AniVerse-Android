package com.example.aniverse.ui.animeLists

import com.example.aniverse.data.repository.PersonalList

data class PersonalListUiState(
    val list: List<PersonalList>,
    val errorMessage: String?=null
)
