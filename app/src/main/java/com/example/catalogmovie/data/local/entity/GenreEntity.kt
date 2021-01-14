package com.example.catalogmovie.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "genres")
data class GenreEntity (
    @PrimaryKey
    @ColumnInfo(name = "genreId")
    val genreId: Int? = 0,
    @ColumnInfo(name = "name")
    val name: String? = ""
): Serializable