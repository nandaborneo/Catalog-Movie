package com.example.catalogmovie.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.catalogmovie.data.MovieRepository
import com.example.catalogmovie.data.local.entity.MovieEntity
import com.example.catalogmovie.data.local.entity.MovieWithGenres
import com.example.catalogmovie.utils.SingleLiveEvent
import com.example.catalogmovie.vo.Resource

class MovieViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
) : ViewModel() {
    var movieList = MutableLiveData<PagedList<MovieWithGenres>>()
    val message = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val page = MutableLiveData<String>()
    val openDetailMovie = SingleLiveEvent<Int>()

    init {
        loading.value = false
        page.value = "1"
    }

    fun getListMovie(genreId: Int): LiveData<Resource<PagedList<MovieWithGenres>>> = movieRepository.getDataDiscoverMovie(
        HashMap<String?, String?>().apply {
            put("page", page.value)
            put("with_genres", genreId.toString())
        })

    fun openDetailMovie(id: Int) {
        openDetailMovie.value = id
    }
}