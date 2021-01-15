package com.example.catalogmovie.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.catalogmovie.data.local.entity.MovieWithGenres
import com.example.catalogmovie.data.remote.paging.MoviePageKeyedDataSource
import com.example.catalogmovie.vo.Resource

data class RepoMovieResult(
    val data: LiveData<PagedList<MovieWithGenres>>? = null,
    val resource: LiveData<Resource<MovieWithGenres>>? = null,
    val sourceLiveData: MutableLiveData<MoviePageKeyedDataSource>? = null
)