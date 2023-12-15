package com.example.aniverse.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface AnimeApi {
    @GET("anime")
    suspend fun getAll( @Query("order_by") orderBy: String = "popularity",
                        @Query("min_score") minScore: Int = 5,
                        @Query("limit") limit: Int=20,
                        @Query("offset") offset:Int=0): AnimeListResponse

    @GET("anime/{id}/themes")
    suspend fun getAnimeThemes(@Path("id") id: Int):ThemesListResponse

    @GET("characters")
    suspend fun getAllCharacters(@Query("order_by") orderBy: String = "favorites",
                                 @Query("sort") sort: String = "desc",
                                 @Query("limit") limit: Int=20,
                                 @Query("offset") offset:Int=0):CharacterListResponse
}

@Singleton
class AnimeService @Inject constructor() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.jikan.moe/v4/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
val api: AnimeApi = retrofit.create(AnimeApi::class.java)
}
