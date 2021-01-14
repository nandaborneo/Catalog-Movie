package com.example.catalogmovie.ui.screen.movielist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogmovie.R
import com.example.catalogmovie.databinding.ActivityMainBinding
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

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var dataIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater).apply {
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
            openDetailMovie.observe(this@MovieListActivity, {
                val intent = Intent(this@MovieListActivity, DetailMovieActivity::class.java)
                intent.putExtra("movieId", it)
                startActivity(intent)
            })
            message.observe(this@MovieListActivity, {
                Toast.makeText(this@MovieListActivity, it, Toast.LENGTH_LONG).show()
            })
            page.observe(this@MovieListActivity,{
                    _ ->

                getListMovie(dataIntent.extras?.getInt("genre_id")!!)
                Log.e("caga","asdsadasd")
            })
            dataIntent.extras?.getInt("genre_id")?.let {
                getListMovie(it).observe(this@MovieListActivity, { listMovie ->
                if (listMovie != null) {
                    when (listMovie.status) {
                        Status.LOADING -> loading.postValue(true)
                        Status.SUCCESS -> {
                            loading.postValue(false)
                            val tempListMovie = listMovie.copy()
                            if((listMovie.data?.size ?: 0) % 2 == 1){
                                tempListMovie.data?.removeLast()
                            }
                            movieList.postValue(listMovie.data)
                            viewBinding.rvMovie.adapter?.let {
                                    adapter ->
                                when (adapter) {
                                    is MovieItemAdapter -> {
                                        adapter.submitList(listMovie.data)
                                        adapter.notifyDataSetChanged()
                                    }
                                    else -> Log.e("Error","NoAdapter")
                                }
                            }
                        }
                        Status.ERROR -> {
                            loading.value = false
                            Toast.makeText(this@MovieListActivity, "Check your internet connection", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
            }
        }
    }

    private fun setUpRecyclerView(){
        viewBinding.vm?.let {
            viewBinding.rvMovie.apply {
                layoutManager = FlexboxLayoutManager(context)
                adapter = MovieItemAdapter(arrayListOf(), it, this@MovieListActivity)
                addOnScrollListener(discoverScrollListener())
            }
        }
    }

    private fun discoverScrollListener() = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            viewBinding.vm?.loading?.value?.let {
                if (it) {
                    return
                }
            }
            if (!recyclerView.canScrollVertically(1)) {
                viewBinding.vm?.page?.value?.apply {
                    viewBinding.vm?.page?.value =
                        (viewBinding.vm?.page?.value?.toInt()?.plus(1)).toString()
                }
                Snackbar.make(viewBinding.clMain, R.string.load_more, Snackbar.LENGTH_LONG).apply {
                    animationMode = Snackbar.ANIMATION_MODE_SLIDE
                    setAction("Hide") {
                        this.dismiss()
                    }.show()
                }
            }
        }
    }
}