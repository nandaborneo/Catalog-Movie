package com.example.catalogmovie.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catalogmovie.data.MovieRepository
import com.example.catalogmovie.data.local.entity.GenreEntity
import com.example.catalogmovie.data.remote.response.MovieDetailResponse
import com.example.catalogmovie.utils.getRuntimeFormatted
import com.example.catalogmovie.vo.Resource
import com.google.gson.JsonArray
import java.lang.Exception

class DetailMovieViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    var movieDetail = MutableLiveData<Resource<MovieDetailResponse>>()
    val loading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    init {
        loading.value = false
    }

    fun getMovieDetail(movieId: Int) {
        try {
            movieDetail = movieRepository.getMovieDetail(
                movieId,
                HashMap<String?, String?>().apply {
                    put("append_to_response", "videos")
                })
        } catch (e: Exception){
        }
    }

    fun getRuntimeFormatted(runtime: Int): String {
        return runtime.getRuntimeFormatted()
    }

    fun getSpokenLanguages(spokenLanguages: JsonArray?): String {
        var result = ""
        spokenLanguages?.forEach { it ->
            result += it.asJsonObject.get("name").asString + ", "
        }
        return "Language : " + result.dropLast(2)
    }
}