package com.example.aniverse.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.example.aniverse.data.repository.Anime
import com.example.aniverse.data.repository.PersonalList

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

@Entity(tableName = "lists")
data class ListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
)

@Entity(tableName = "animelist", foreignKeys = [
    ForeignKey(
        entity = ListEntity::class,
        parentColumns = ["id"],
        childColumns = ["listId"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = AnimeEntity::class,
        parentColumns = ["mal_id"],
        childColumns = ["animeId"],
    )
])
data class AnimeListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val listId: Int,
    val animeId: Int,
)

/*@Entity(tableName = "animeLocal")
data class AnimeLocalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userScore:Int,
    val animeId: Int
)*/

fun List<AnimeEntity>.asAnime():List<Anime> {
    return this.map {
        Anime(it.mal_id, it.title, it.status, it.episodes, it.score, it.image_url)
    }
}

fun List<ListEntity>.asPersonalList():List<PersonalList> {
    return this.map {
        PersonalList(it.id, it.name)
    }
}

fun AnimeEntity.asAnime():Anime {
    return Anime(this.mal_id, this.title, this.status, this.episodes, this.score, this.image_url)
}



