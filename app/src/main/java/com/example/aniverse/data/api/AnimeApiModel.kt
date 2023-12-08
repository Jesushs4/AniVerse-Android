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
