package com.example.catalogmovie.ui.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.catalogmovie.data.MovieRepository
import com.example.catalogmovie.data.RepoMovieResult
import com.example.catalogmovie.data.local.entity.GenreEntity
import com.example.catalogmovie.data.local.entity.MovieWithGenres
import com.example.catalogmovie.utils.SingleLiveEvent
import com.example.catalogmovie.vo.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MovieViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
) : ViewModel() {
    var movieList: LiveData<PagedList<MovieWithGenres>>? = null
    val message = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val openDetailMovie = SingleLiveEvent<Int>()
    var repoMoviesResult: LiveData<RepoMovieResult?>? = null
    var genre = MutableLiveData<HashMap<String?, String?>>()
    var genreList: List<GenreEntity>
    var networkState: LiveData<Resource<MovieWithGenres>>? = null

    init {
        loading.value = false
        genreList = movieRepository.getSavedGenre()
    }

    fun getPagedListMovie(genreId: Int) {
        genre.value = HashMap<String?,String?>().apply {
            put("with_genres", genreId.toString())
        }
        repoMoviesResult =
            Transformations.map(genre) { map -> movieRepository.getDiscoverMovie(map) }

        repoMoviesResult?.let {
            movieList = Transformations.switchMap(
                it
            ) { input -> input?.data }
            networkState = Transformations.switchMap(it) { input -> input?.resource }
        }

    }

    fun openDetailMovie(id: Int) {
        openDetailMovie.value = id
    }
}