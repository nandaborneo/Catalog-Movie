package com.example.catalogmovie.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.catalogmovie.data.local.LocalDataSource
import com.example.catalogmovie.data.local.entity.GenreEntity
import com.example.catalogmovie.data.local.entity.MovieEntity
import com.example.catalogmovie.data.local.entity.MovieGenreCrossRef
import com.example.catalogmovie.data.local.entity.MovieWithGenres
import com.example.catalogmovie.data.remote.ApiResponse
import com.example.catalogmovie.data.remote.NetworkBoundResource
import com.example.catalogmovie.data.remote.RemoteDataSource
import com.example.catalogmovie.data.remote.response.DiscoverResponse
import com.example.catalogmovie.data.remote.response.GenreListResponse
import com.example.catalogmovie.data.remote.response.MovieDetailResponse
import com.example.catalogmovie.utils.AppExecutors
import com.example.catalogmovie.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) {

    companion object{
        const val PAGE_SIZE = 100
        const val GENRE_PAGE_SIZE = 4
    }

    private var PAGE = 0

    fun getSavedGenre(): List<GenreEntity> = localDataSource.getPlainAllGenre()

    fun getDataGenre(): LiveData<Resource<PagedList<GenreEntity>>> =
        object : NetworkBoundResource<PagedList<GenreEntity>, GenreListResponse>(appExecutors){
            override fun loadFromDB(): LiveData<PagedList<GenreEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(GENRE_PAGE_SIZE)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllGenre(), config).build()
            }

            override fun shouldFetch(data: PagedList<GenreEntity>?): Boolean
                = data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<GenreListResponse>>
                = remoteDataSource.getGenreMovie()

            override fun saveCallResult(data: GenreListResponse) {
                val genreList = ArrayList<GenreEntity>()
                data.genres?.let {
                    for (item in it){
                        val genre = GenreEntity(item.id, item.name)
                        genreList.add(genre)
                    }
                    localDataSource.addGenre(genreList)
                }
            }
        }.asLiveData()

    fun getDiscoverMovie(map: HashMap<String?, String?>): RepoMovieResult {
        return remoteDataSource.getDiscoverMoviePagedStyle(map)
    }

    fun getDataDiscoverMovie(
        map: HashMap<String?, String?>
    ): LiveData<Resource<PagedList<MovieWithGenres>>> {

        return object : NetworkBoundResource<PagedList<MovieWithGenres>, DiscoverResponse>(appExecutors){
            override fun loadFromDB(): LiveData<PagedList<MovieWithGenres>> {
                Log.e("PAGE1", PAGE.toString() +"|"+ map["page"]?.toInt())
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setPageSize(PAGE_SIZE)
                    .build()
                map["with_genres"]?.let {
                    return LivePagedListBuilder(localDataSource.getMovByGenre(it.toInt()), config).build()
                }
                return LivePagedListBuilder(localDataSource.getAllMov(), config).build()
            }

            override fun shouldFetch(data: PagedList<MovieWithGenres>?): Boolean{
                Log.e("tes",(PAGE != map["page"]?.toInt() ?: false).toString())
                return data == null || data.isEmpty() || (PAGE != map["page"]?.toInt() ?: false)
            }

            override fun createCall(): LiveData<ApiResponse<DiscoverResponse>>{
                Log.e("PAGE3", PAGE.toString() +"|"+ map["page"]?.toInt())
                return remoteDataSource.getDiscoverMovie(map)
            }

            override fun saveCallResult(data: DiscoverResponse){
                Log.e("4", data.toString())
                val movieList = ArrayList<MovieEntity>()
                val movieGenreList = ArrayList<MovieGenreCrossRef>()
                data.results?.let {
                    for (item in it){
                        val movie = MovieEntity(
                            item.id,
                            item.adult,
                            item.backdrop_path,
                            item.original_language,
                            item.original_title,
                            item.overview,
                            item.popularity,
                            item.poster_path,
                            item.release_date,
                            item.title,
                            item.video,
                            item.vote_average,
                            item.vote_count,
                        )
                        movieList.add(movie)
                        for (genreItem in item.genre_ids){
                            movie.movieId?.let {
                                val movieGenreCrossRef = MovieGenreCrossRef(movie.movieId,genreItem)
                                movieGenreList.add(movieGenreCrossRef)
                            }
                        }
                    }
                    localDataSource.addMov(movieList)
                    localDataSource.addMovGenre(movieGenreList)
                }
            }
        }.asLiveData()
    }

    fun getMovieDetail(movieId: Int, map: HashMap<String?, String?>): MutableLiveData<Resource<MovieDetailResponse>>
        = remoteDataSource.getMovieDetail(movieId,map)


}