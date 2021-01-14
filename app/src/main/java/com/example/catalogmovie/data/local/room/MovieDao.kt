package com.example.catalogmovie.data.local.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.catalogmovie.data.local.entity.*

@Dao
interface MovieDao {
    @Insert(onConflict = REPLACE, entity = MovieEntity::class)
    fun addMovie(listMovie: List<MovieEntity>)

    @Insert(onConflict = REPLACE, entity = MovieGenreCrossRef::class)
    fun addMovGenre(listMovGenre: List<MovieGenreCrossRef>)

    @Insert(onConflict = REPLACE, entity = GenreEntity::class)
    fun addGenre(listGenre: List<GenreEntity>)

    @Query("SELECT * FROM genres where genreId = :id")
    fun getGenre(id: Int) : GenreEntity

    @Transaction
    @Query("SELECT * FROM  movies")
    fun getMovieWithGenre(): DataSource.Factory<Int, MovieWithGenres>

    @Transaction
    @Query("SELECT * FROM movies a JOIN movie_genre b ON a.movieId = b.movieId AND b.genreId = :genreId")
    fun getMovieByGenre(genreId: Int): DataSource.Factory<Int, MovieWithGenres>

    @Query("SELECT * FROM genres")
    fun getGenre(): DataSource.Factory<Int, GenreEntity>
}