package com.example.catalogmovie.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.catalogmovie.data.MovieRepository
import com.example.catalogmovie.data.local.entity.GenreEntity
import com.example.catalogmovie.utils.SingleLiveEvent
import com.example.catalogmovie.vo.Resource

class GenreViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
): ViewModel() {

    var genreList = MutableLiveData<PagedList<GenreEntity>>()
    val loading = MutableLiveData<Boolean>()
    val openListMovieByGenre = SingleLiveEvent<Int>()

    init {
        loading.value = false
    }

    fun getGenreMovie(): LiveData<Resource<PagedList<GenreEntity>>> = movieRepository.getDataGenre()

    fun openMovieByGenre(genreId: Int) {
        openListMovieByGenre.value = genreId
    }
}