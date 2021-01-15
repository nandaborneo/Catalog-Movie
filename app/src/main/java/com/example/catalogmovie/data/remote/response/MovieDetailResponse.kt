package com.example.catalogmovie.data.remote.response

import com.example.catalogmovie.data.local.entity.GenreEntity
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.Serializable

data class MovieDetailResponse(
    val adult: Boolean? = null,
    val backdrop_path: String? = "",
    val belongs_to_collection: Any? = null,
    val budget: Int? = 0,
    val genres: MutableList<GenreEntity>? = arrayListOf(),
    val homepage: String? = "",
    val id: Int? = 0,
    val imdb_id: String? = "",
    val original_language: String? = "",
    val original_title: String? = "",
    val overview: String? = "",
    val popularity: Double? = 0.0,
    var poster_path: String? = "",
    val release_date: String? = "",
    val revenue: Int? = 0,
    val runtime: Int? = 0,
    val spoken_languages: JsonArray? = null,
    val status: String? = "",
    val tagline: String? = "",
    val title: String? = "",
    val video: Boolean? = null,
    val vote_average: Double? = 0.0,
    val vote_count: Int? = 0,
    val videos: JsonObject? = null,
) : Serializable