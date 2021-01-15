package com.example.catalogmovie.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.catalogmovie.api.ApiService
import com.example.catalogmovie.data.RepoMovieResult
import com.example.catalogmovie.data.local.LocalDataSource
import com.example.catalogmovie.data.local.entity.MovieWithGenres
import com.example.catalogmovie.data.remote.paging.MovieDataSourceFactory
import com.example.catalogmovie.data.remote.response.DiscoverResponse
import com.example.catalogmovie.data.remote.response.GenreListResponse
import com.example.catalogmovie.data.remote.response.MovieDetailResponse
import com.example.catalogmovie.utils.AppExecutors
import com.example.catalogmovie.utils.EspressoIdlingResource
import com.example.catalogmovie.vo.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.await
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) {

    fun getDiscoverMoviePagedStyle(
        map: HashMap<String?, String?>
    ): RepoMovieResult {
        EspressoIdlingResource.increment()
        map["with_genres"]?.toInt()?.let {
            val sourceFactory = MovieDataSourceFactory(
                apiService,
                localDataSource,
                appExecutors.networkIO(),
                it
            )

            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(Companion.PAGE_SIZE)
                .build()

            val movieList: LiveData<PagedList<MovieWithGenres>> = LivePagedListBuilder(
                sourceFactory, config
            ).setFetchExecutor(appExecutors.networkIO())
                .build()

            val networkState: LiveData<Resource<MovieWithGenres>> =
                Transformations.switchMap(
                    sourceFactory.sourceLiveData
                ) { input -> input.networkState }

            return RepoMovieResult(
                movieList,
                networkState,
                sourceFactory.sourceLiveData
            )
        }
        return RepoMovieResult()
    }

    fun getDiscoverMovie(
        map: HashMap<String?, String?>
    ): LiveData<ApiResponse<DiscoverResponse>> {

        EspressoIdlingResource.increment()

        val list = MutableLiveData<ApiResponse<DiscoverResponse>>()

        CoroutineScope(IO).launch {
            try {
                list.postValue(
                    ApiResponse.success(
                        apiService.getDiscoverMovie(queries = map).await()
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
                list.postValue(
                    ApiResponse.error(
                        e.message.toString(),
                        DiscoverResponse()
                    )
                )
            }
        }
        EspressoIdlingResource.decrement()
        return list
    }

    fun getMovieDetail(movieId: Int, map: HashMap<String?, String?>): MutableLiveData<Resource<MovieDetailResponse>> {
        EspressoIdlingResource.increment()
        var movieDetailResponse = MutableLiveData<Resource<MovieDetailResponse>>()
        movieDetailResponse.postValue(Resource.loading(null))
        CoroutineScope(IO).launch {
            try {
                movieDetailResponse.postValue(Resource.success(apiService.getMovieDetails(movieId, map).await()))
            } catch (e: Exception) {
                movieDetailResponse.postValue(Resource.error(e.localizedMessage,null))
            }
        }
        return movieDetailResponse
    }

    fun getGenreMovie(): LiveData<ApiResponse<GenreListResponse>> {
        EspressoIdlingResource.increment()

        val list = MutableLiveData<ApiResponse<GenreListResponse>>()

        CoroutineScope(IO).launch {
            try {
                list.postValue(ApiResponse.success(apiService.getGenreMovie().await()))
            } catch (e: IOException) {
                e.printStackTrace()
                list.postValue(
                    ApiResponse.error(
                        e.message.toString(),
                        GenreListResponse()
                    )
                )
            }
        }
        EspressoIdlingResource.decrement()
        return list
    }

    companion object {
        private val PAGE_SIZE: Int = 20
    }
}