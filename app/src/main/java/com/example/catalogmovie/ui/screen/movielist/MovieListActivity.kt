package com.example.catalogmovie.ui.screen.movielist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogmovie.R
import com.example.catalogmovie.databinding.ActivityMovieListBinding
import com.example.catalogmovie.ui.adapter.MovieItemAdapter
import com.example.catalogmovie.ui.screen.detail.DetailMovieActivity
import com.example.catalogmovie.ui.viewmodel.MovieViewModel
import com.example.catalogmovie.utils.obtainViewModel
import com.example.catalogmovie.vo.Status
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMovieListBinding
    private lateinit var dataIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMovieListBinding.inflate(layoutInflater).apply {
            vm = obtainViewModel(MovieViewModel::class.java)
        }
        viewBinding.lifecycleOwner = this
        setContentView(viewBinding.root)

        dataIntent = intent

        setUpObserver()
        setUpRecyclerView()
        
    }

    private fun setUpObserver() {
        viewBinding.vm?.apply {
            dataIntent.extras?.getInt("genre_id")?.let {
                getPagedListMovie(it)
            }
            openDetailMovie.observe(this@MovieListActivity, {
                val intent = Intent(this@MovieListActivity, DetailMovieActivity::class.java)
                intent.putExtra("movieId", it)
                startActivity(intent)
            })
            message.observe(this@MovieListActivity, {
                Toast.makeText(this@MovieListActivity, it, Toast.LENGTH_LONG).show()
            })
            networkState?.observe(this@MovieListActivity, {
                when (it.status) {
                    Status.LOADING -> {
                        if (movieList?.value?.size?: 0<=0)
                            loading.postValue(true)
                    }
                    Status.SUCCESS -> loading.postValue(false)
                    Status.ERROR -> loading.postValue(false)
                }
            })
        }
    }

    private fun setUpRecyclerView(){
        viewBinding.vm?.let {
            viewBinding.rvMovie.apply {

                layoutManager = GridLayoutManager(context,2)
                adapter = MovieItemAdapter(arrayListOf(), it, this@MovieListActivity)
            }
        }
    }
}