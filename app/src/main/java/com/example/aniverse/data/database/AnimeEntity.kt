package com.example.aniverse.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aniverse.data.repository.Anime

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey
    val mal_id: Int,
    val title: String,
    val status: String,
    val episodes: Int,
    val score: Double,
    val image_url: String
)

fun List<AnimeEntity>.asAnime():List<Anime> {
    return this.map {
        Anime(it.mal_id, it.title, it.status, it.episodes, it.score, it.image_url)
    }
}

fun AnimeEntity.asAnime():Anime {
    return Anime(this.mal_id, this.title, this.status, this.episodes, this.score, this.image_url)
}