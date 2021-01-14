package com.example.catalogmovie.data.local

import androidx.paging.DataSource
import com.example.catalogmovie.data.local.entity.*
import com.example.catalogmovie.data.local.room.MovieDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(movieDatabase: MovieDatabase) {
    var movieDao = movieDatabase.MovieDao()

    fun addMov(
        listMovie: List<MovieEntity>
    ) = movieDao.addMovie(listMovie)

    fun addMovGenre(
        listMovGenre: List<MovieGenreCrossRef>
    ) = movieDao.addMovGenre(listMovGenre)

    fun addGenre(
        listGenre: List<GenreEntity>
    ) = movieDao.addGenre(listGenre)

    fun getGenre(
        id: Int
    ) = movieDao.getGenre(id)

    fun getAllGenre(): DataSource.Factory<Int, GenreEntity> = movieDao.getGenre()

    fun getMovByGenre(
        genreId: Int
    ): DataSource.Factory<Int, MovieWithGenres> = movieDao.getMovieByGenre(genreId)

    fun getAllMov(): DataSource.Factory<Int, MovieWithGenres> = movieDao.getMovieWithGenre()
}