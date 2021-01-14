package com.example.catalogmovie.di

import android.content.Context
import androidx.room.Room
import com.example.catalogmovie.data.local.room.MovieDatabase
import com.example.catalogmovie.utils.Constant.Companion.LOCAL_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java, LOCAL_DATABASE
        ).build()
    }
}