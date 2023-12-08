package com.example.aniverse.di

import android.content.Context
import com.example.aniverse.data.database.AnimeDao
import com.example.aniverse.data.database.AnimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAnimeDatabase(@ApplicationContext context: Context): AnimeDatabase {
        return AnimeDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideAnimeDao(database: AnimeDatabase): AnimeDao {
        return database.animeDao()
    }
}