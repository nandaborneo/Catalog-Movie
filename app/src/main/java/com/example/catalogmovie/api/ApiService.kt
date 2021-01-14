package com.example.catalogmovie.api

import com.example.catalogmovie.BuildConfig
import com.example.catalogmovie.data.remote.response.DiscoverResponse
import com.example.catalogmovie.data.remote.response.GenreListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @GET("discover/movie")
    fun getDiscoverMovie(
        @Query("api_key") apiKey: String = BuildConfig.API_TOKEN,
        @QueryMap queries: HashMap<String?, String?>?
    ): Call<DiscoverResponse>

    @GET("genre/movie/list")
    fun getGenreMovie(
        @Query("api_key") apiKey: String = BuildConfig.API_TOKEN,
    ): Call<GenreListResponse>
}