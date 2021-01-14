package com.example.catalogmovie.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class GenreWithMovies(
    @Embedded val genre: GenreEntity,
    @Relation(
        parentColumn = "genreId",
        entity = GenreEntity::class,
        entityColumn = "movieId",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val movieList: List<MovieEntity>
)