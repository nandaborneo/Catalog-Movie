package com.example.catalogmovie.data.remote.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.catalogmovie.api.ApiService
import com.example.catalogmovie.data.local.LocalDataSource
import com.example.catalogmovie.data.local.entity.GenreEntity
import com.example.catalogmovie.data.local.entity.MovieEntity
import com.example.catalogmovie.data.local.entity.MovieWithGenres
import com.example.catalogmovie.data.remote.response.DiscoverResponse
import com.example.catalogmovie.data.remote.response.MovieItemReponse
import com.example.catalogmovie.vo.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import javax.inject.Inject

class MoviePageKeyedDataSource (
    private val apiService: ApiService,
    private val networkExecutor: Executor,
    val genreId: Int
) : PageKeyedDataSource<Int, MovieWithGenres>() {

    var networkState: MutableLiveData<Resource<MovieWithGenres>> =
        MutableLiveData<Resource<MovieWithGenres>>()

    var retryCallback: RetryCallback? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieWithGenres?>
    ) {
        networkState.postValue(Resource.loading(null))

        val request = apiService.getDiscoverMovie(queries = HashMap<String?, String?>().apply {
            put("page", FIRST_PAGE)
            put("with_genres", genreId.toString())
        })

        try {
            val response = request.execute()
            val listMovie: List<MovieItemReponse> =
                response.body()?.results ?: emptyList<MovieItemReponse>()
            val result = arrayListOf<MovieWithGenres>()
            for (movie in listMovie){
                val movieEntity = MovieEntity(
                    movieId = movie.id,
                    adult = movie.adult,
                    backdrop_path = movie.backdrop_path,
                    original_language = movie.original_language,
                    original_title = movie.original_title,
                    overview = movie.overview,
                    popularity = movie.popularity,
                    poster_path = movie.poster_path,
                    release_date = movie.release_date,
                    title = movie.title,
                    video = movie.video,
                    vote_average = movie.vote_average,
                    vote_count = movie.vote_count,
                )
                val listGenre = arrayListOf<GenreEntity>()
                for (genre in movie.genre_ids){
                    listGenre.add(GenreEntity(genre))
                }
                result.add(MovieWithGenres(movieEntity,listGenre))
            }
            callback.onResult(result.toList(), null, FIRST_PAGE.toInt() + 1)
        } catch (e: Exception) {

            retryCallback = object : RetryCallback {
                override operator fun invoke() {
                    networkExecutor.execute(Runnable { loadInitial(params, callback) })
                }
            }
            networkState.postValue(Resource.error(e.message, null))
        }

    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MovieWithGenres?>
    ) {
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MovieWithGenres?>
    ) {
        networkState.postValue(Resource.loading(null))

        val request = apiService.getDiscoverMovie(queries = HashMap<String?, String?>().apply {
            put("page", params.key.toString())
            put("with_genres", genreId.toString())
        })

        request.enqueue(object : Callback<DiscoverResponse> {
            override fun onResponse(
                call: Call<DiscoverResponse>,
                response: Response<DiscoverResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    val movieList: List<MovieItemReponse>? =
                        if (data != null) data.results else emptyList()

                    retryCallback = null
                    movieList?.let {
                        val result = arrayListOf<MovieWithGenres>()
                        for (movie in movieList){
                            val movieEntity = MovieEntity(
                                movieId = movie.id,
                                adult = movie.adult,
                                backdrop_path = movie.backdrop_path,
                                original_language = movie.original_language,
                                original_title = movie.original_title,
                                overview = movie.overview,
                                popularity = movie.popularity,
                                poster_path = movie.poster_path,
                                release_date = movie.release_date,
                                title = movie.title,
                                video = movie.video,
                                vote_average = movie.vote_average,
                                vote_count = movie.vote_count,
                            )
                            val listGenre = arrayListOf<GenreEntity>()
                            for (genre in movie.genre_ids){
                                listGenre.add(GenreEntity(genre))
                            }
                            result.add(MovieWithGenres(movieEntity,listGenre))
                        }
                        callback.onResult(result.toList(), params.key + 1)
                    }
                    networkState.postValue(Resource.success(null))
                } else {
                    retryCallback = object : RetryCallback {
                        override operator fun invoke() {
                            loadAfter(params, callback)
                        }
                    }
                    networkState.postValue(Resource.error("error code: " + response.code(), null))
                }
            }

            override fun onFailure(call: Call<DiscoverResponse>, t: Throwable) {
                retryCallback = object : RetryCallback {
                    override operator fun invoke() {
                        networkExecutor.execute { loadAfter(params, callback) }
                    }
                }
                networkState.postValue(
                    Resource.error(
                        t.message,
                        null
                    )
                )
            }

        })


    }

    companion object {
        const val FIRST_PAGE = "1"
    }

    interface RetryCallback {
        fun invoke()
    }
}
