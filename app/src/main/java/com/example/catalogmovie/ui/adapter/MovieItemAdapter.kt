package com.example.catalogmovie.ui.adapter

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowMetrics
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogmovie.R
import com.example.catalogmovie.data.local.entity.GenreEntity
import com.example.catalogmovie.data.local.entity.MovieWithGenres
import com.example.catalogmovie.databinding.GenreItemBinding
import com.example.catalogmovie.databinding.MovieItemBinding
import com.example.catalogmovie.ui.listener.ItemClickListener
import com.example.catalogmovie.ui.viewmodel.GenreViewModel
import com.example.catalogmovie.ui.viewmodel.MovieViewModel
import com.example.catalogmovie.utils.getSizeByScreenSize
import com.example.catalogmovie.utils.toImageUrl

class MovieItemAdapter constructor(
    private var movieList: MutableList<MovieWithGenres>,
    private val viewModel: MovieViewModel,
    val context: Activity
) : PagedListAdapter<MovieWithGenres, MovieItemAdapter.MovieItemHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemHolder {
        return MovieItemHolder(
            context,
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.movie_item,
                parent,
                false
            ),
            viewModel
        )
    }

    override fun onBindViewHolder(holder: MovieItemHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this, object : ItemClickListener {
                override fun onItemClick() {
                    movie.movieId?.let {
                        viewModel.openDetailMovie(it)
                    }
                }
            })
        }
    }

    class MovieItemHolder constructor(
        val context: Activity,
        val binding: MovieItemBinding,
        val viewModel: MovieViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieItem: MovieWithGenres, mainActionListener: ItemClickListener) {
            binding.movieImage.clipToOutline = true
            binding.movieItem = movieItem
            binding.action = mainActionListener
            val genreList: MutableList<GenreEntity> = arrayListOf()
            for (genreItem in movieItem.genreLists){
                genreList.add(viewModel.genreList.first { genreEntity -> genreEntity.genreId == genreItem.genreId })
            }
            binding.rvMovieGenre.adapter = GenreMovieItemAdapter(genreList)
            binding.executePendingBindings()
        }


    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieWithGenres>() {
            override fun areItemsTheSame(
                oldItem: MovieWithGenres,
                newItem: MovieWithGenres
            ): Boolean {
                return oldItem.movie.movieId == newItem.movie.movieId
            }

            override fun areContentsTheSame(
                oldItem: MovieWithGenres,
                newItem: MovieWithGenres
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}