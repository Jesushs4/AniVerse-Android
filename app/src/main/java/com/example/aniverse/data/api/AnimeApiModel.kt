package com.example.aniverse.data.api

import com.example.aniverse.data.database.AnimeEntity

data class AnimeApiModel(
    val mal_id: Int,
    val title: String,
    val status: String,
    val episodes: Int,
    val score: Double,
    val image_url: String
)

data class AnimeApiListModel(
    val anime: List<AnimeApiModel>
)

data class AnimeListResponse(
    val data: List<AnimeListItemResponse>
)


data class ThemesApiModel(
    val openings: List<String> = emptyList(),
    val endings: List<String> = emptyList()
)

data class ThemesItemResponse(
    val openings: List<String>,
    val endings: List<String>
)
data class ThemesListResponse(
    val data: ThemesItemResponse
)

data class AnimeListItemResponse(
    val mal_id:Int,
    val title:String,
    val status: String,
    val episodes: Int,
    val score: Double,
    val images: AnimeJpgResponse
)

data class AnimeJpgResponse(
    val jpg: AnimeImageResponse
)

data class AnimeImageResponse(
    val image_url: String
)

data class CharacterApiModel(
    val mal_id: Int,
    val name:String,
    val images: String
)

data class CharacterListResponse(
    val data: List<CharacterItemResponse>
)
data class CharacterItemResponse(
    val mal_id: Int,
    val name: String,
    val images: CharacterJpgResponse
)
data class CharacterJpgResponse(
    val jpg: CharacterImageResponse
)

data class CharacterImageResponse(
    val image_url: String
)

fun List<AnimeApiModel>.asEntityModel(): List<AnimeEntity> {
    return this.map {
        AnimeEntity(
            it.mal_id,
            it.title,
            it.status,
            it.episodes,
            it.score,
            it.image_url
        )
    }
}
