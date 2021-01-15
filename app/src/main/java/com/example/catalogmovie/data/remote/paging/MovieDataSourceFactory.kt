package com.example.catalogmovie.data.remote.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.catalogmovie.api.ApiService
import com.example.catalogmovie.data.local.LocalDataSource
import com.example.catalogmovie.data.local.entity.MovieWithGenres
import java.util.concurrent.Executor

class MovieDataSourceFactory(
    private val apiService: ApiService,
    private val localDataSource: LocalDataSource,
    private val executor: Executor,
    private val genreId: Int
) : DataSource.Factory<Int, MovieWithGenres>() {

    var sourceLiveData = MutableLiveData<MoviePageKeyedDataSource>()

    override fun create(): DataSource<Int, MovieWithGenres> {
        val movieKeyedSourse =
            MoviePageKeyedDataSource(apiService, executor, genreId)
        sourceLiveData.postValue(movieKeyedSourse)
        return movieKeyedSourse
    }

}