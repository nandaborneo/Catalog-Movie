package com.example.catalogmovie.ui.screen.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.catalogmovie.R
import com.example.catalogmovie.databinding.ActivityDetailMovieBinding
import com.example.catalogmovie.ui.adapter.GenreMovieItemAdapter
import com.example.catalogmovie.ui.dialog.VideoDialog
import com.example.catalogmovie.ui.viewmodel.DetailMovieViewModel
import com.example.catalogmovie.utils.EspressoIdlingResource
import com.example.catalogmovie.utils.obtainViewModel
import com.example.catalogmovie.vo.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMovieActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityDetailMovieBinding
    lateinit var dataIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDetailMovieBinding.inflate(layoutInflater).apply {
            vm = obtainViewModel(DetailMovieViewModel::class.java)
        }
        viewBinding.lifecycleOwner = this
        setContentView(viewBinding.root)

        dataIntent = intent

        setUpObserver()
        setUpRecyclerView()
    }

    private fun setUpObserver() {
        viewBinding.vm?.apply {
            getMovieDetail(dataIntent.getIntExtra("movieId",0))

            message.observe(this@DetailMovieActivity, {
                Toast.makeText(this@DetailMovieActivity, it, Toast.LENGTH_LONG).show()
            })
            movieDetail.observe(this@DetailMovieActivity,{
                it?.status?.apply {
                    when(it.status){
                        Status.LOADING -> loading.postValue(true)
                        Status.SUCCESS -> {
                            loading.postValue(false)
                        }
                    }
                }
            })
        }

        viewBinding.playTrailer.setOnClickListener {
            viewBinding.vm?.movieDetail?.value?.data?.videos?.let {
                if (it.getAsJsonArray("results").size() == 0) return@let
                it.getAsJsonArray("results")?.get(0)?.asJsonObject?.get("key")?.asString?.apply {
                    EspressoIdlingResource.increment()
                    VideoDialog(this).show(supportFragmentManager, "Video Dialog")
                }
                return@setOnClickListener
            }
            Toast.makeText(this, "Sorry trailer not found!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setUpRecyclerView() {
        viewBinding.rvMovieGenre.adapter = GenreMovieItemAdapter(arrayListOf())
    }
}