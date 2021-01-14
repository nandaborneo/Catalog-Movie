package com.example.catalogmovie.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catalogmovie.data.local.entity.GenreEntity
import com.example.catalogmovie.data.local.entity.MovieEntity
import com.example.catalogmovie.data.local.entity.MovieGenreCrossRef

@Database(entities = [MovieEntity::class, GenreEntity::class, MovieGenreCrossRef::class,], version = 2, exportSchema = false)
abstract class MovieDatabase: RoomDatabase(){
    abstract fun MovieDao(): MovieDao
}