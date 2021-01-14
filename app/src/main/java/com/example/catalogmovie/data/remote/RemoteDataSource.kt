package com.example.catalogmovie.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.catalogmovie.api.ApiService
import com.example.catalogmovie.data.remote.response.DiscoverResponse
import com.example.catalogmovie.data.remote.response.GenreListResponse
import com.example.catalogmovie.utils.EspressoIdlingResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.await
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    fun getDiscoverMovie(
        map: HashMap<String?, String?>
    ): LiveData<ApiResponse<DiscoverResponse>>{
        EspressoIdlingResource.increment()

        val list = MutableLiveData<ApiResponse<DiscoverResponse>>()

        CoroutineScope(IO).launch {
            try {
                list.postValue(ApiResponse.success(apiService.getDiscoverMovie(queries = map).await()))
            }catch (e: IOException){
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

    fun getGenreMovie(): LiveData<ApiResponse<GenreListResponse>>{
        EspressoIdlingResource.increment()

        val list = MutableLiveData<ApiResponse<GenreListResponse>>()

        CoroutineScope(IO).launch {
            try {
                list.postValue(ApiResponse.success(apiService.getGenreMovie().await()))
            }catch (e: IOException){
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
}