package com.example.aniverse.data.repository

import android.util.Log
import com.example.aniverse.data.api.AnimeApiRepository
import com.example.aniverse.data.api.CharacterApiModel
import com.example.aniverse.data.api.ThemesApiModel
import com.example.aniverse.data.api.asEntityModel
import com.example.aniverse.data.database.AnimeDBRepository
import com.example.aniverse.data.database.AnimeListEntity
import com.example.aniverse.data.database.ListEntity
import com.example.aniverse.data.database.ListWithAnimes
import com.example.aniverse.data.database.asAnime
import com.example.aniverse.data.database.asPersonalList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AnimeRepository @Inject constructor(
    private val dbRepository: AnimeDBRepository,
    private val apiRepository: AnimeApiRepository
) {
    val anime: Flow<List<Anime>>
        get() {
            val list = dbRepository.allAnime.map {
                it.asAnime()
            }
            return list
        }

    val lists: Flow<List<PersonalList>>
        get() {
            val list = dbRepository.allList.map {
                it.asPersonalList()
            }
            return list
        }

    suspend fun refreshList() {
        withContext(Dispatchers.IO) {
            val apiAnime = apiRepository.getAll()
            dbRepository.insert(apiAnime.asEntityModel())
        }
    }


    suspend fun getAnimeThemes(id: Int):Flow<ThemesApiModel>  {
        val themesApiModel = apiRepository.getAnimeThemes(id)
        return flowOf(themesApiModel)
    }

    suspend fun getAllCharacters():Flow<List<CharacterApiModel>> {
        val charactersApiModel = apiRepository.getAllCharacters()
        return flowOf(charactersApiModel)
    }

    suspend fun insertList(listEntity: ListEntity) = dbRepository.insertList(listEntity)


    suspend fun insertAnimeList(animeListEntity: AnimeListEntity) = dbRepository.insertAnimeList(animeListEntity)


    fun getAnimeList(id: Int):Flow<ListWithAnimes> {
        return dbRepository.getAnimeList(id)
    }

    suspend fun updateListName(id: Int, name: String) = dbRepository.updateListName(id, name)

    suspend fun deleteList(id:Int) = dbRepository.deleteList(id)


    suspend fun animeDetail(id:Int) = dbRepository.animeDetail(id)

    suspend fun deleteAnimeFromList(mal_id: Int) = dbRepository.deleteAnimeFromList(mal_id)

    //suspend fun updateUserScore(id:Int, newScore:Int) = dbRepository.updateUserScore(id, newScore)
}