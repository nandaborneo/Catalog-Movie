package com.example.catalogmovie.ui.screen.genrelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.catalogmovie.R
import com.example.catalogmovie.databinding.ActivityGenreListBinding
import com.example.catalogmovie.ui.adapter.GenreItemAdapter
import com.example.catalogmovie.ui.screen.movielist.MovieListActivity
import com.example.catalogmovie.ui.viewmodel.GenreViewModel
import com.example.catalogmovie.utils.obtainViewModel
import com.example.catalogmovie.vo.Status
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenreListActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityGenreListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityGenreListBinding.inflate(layoutInflater).apply {
            vm = obtainViewModel(GenreViewModel::class.java)
        }

        viewBinding.lifecycleOwner = this
        setContentView(viewBinding.root)

        setUpObserver()
        setUpRecyclerView()
    }

    private fun setUpObserver() {
        viewBinding.vm?.apply {
            openListMovieByGenre.observe(this@GenreListActivity, {
                val intent = Intent(this@GenreListActivity, MovieListActivity::class.java)
                intent.putExtra("genre_id", it)
                startActivity(intent)
            })

            getGenreMovie().observe(this@GenreListActivity, { listMovie ->
                listMovie?.let {
                    when (listMovie.status) {
                        Status.LOADING -> loading.value = true
                        Status.SUCCESS -> {
                            loading.postValue(false)
                            genreList.postValue(listMovie.data)
                            viewBinding.rvGenre.adapter?.let { adapter ->
                                when (adapter) {
                                    is GenreItemAdapter -> {
                                        adapter.submitList(listMovie.data)
                                        adapter.notifyDataSetChanged()
                                    }
                                    else -> Log.e("Error", "NoAdapter")
                                }
                            }
                        }
                        Status.ERROR -> {
                            loading.value = false
                            Toast.makeText(
                                this@GenreListActivity,
                                "Check your internet connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
        }
    }

    private fun setUpRecyclerView() {
        viewBinding.vm?.let {
            viewBinding.rvGenre.apply {
                layoutManager = GridLayoutManager(context,2)
                adapter = GenreItemAdapter(arrayListOf(), it, this@GenreListActivity)
            }
        }
    }
}