package com.example.catalogmovie.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogmovie.data.local.entity.GenreEntity
import com.example.catalogmovie.data.local.entity.MovieEntity
import com.example.catalogmovie.data.local.entity.MovieWithGenres
import com.example.catalogmovie.ui.adapter.GenreItemAdapter
import com.example.catalogmovie.ui.adapter.GenreMovieItemAdapter
import com.example.catalogmovie.ui.adapter.MovieItemAdapter
import com.example.catalogmovie.utils.load
import com.facebook.shimmer.ShimmerFrameLayout

object GlobalBinding {

    @BindingAdapter("loadImage")
    @JvmStatic
    fun setImage(imageView: ImageView, url: String?) {
        url?.let {
            imageView.load(it)
        }
    }
    @BindingAdapter("genreList")
    @JvmStatic
    fun setGenreList(recyclerView: RecyclerView, dataList: PagedList<GenreEntity>?) {
        dataList?.let {
            (recyclerView.adapter as GenreItemAdapter).apply {
                submitList(it)
                notifyDataSetChanged()
            }
        }
    }


    @BindingAdapter("genreList")
    @JvmStatic
    fun setGenreList(recyclerView: RecyclerView, dataList: MutableList<GenreEntity>?) {
        dataList?.let {
            (recyclerView.adapter as GenreMovieItemAdapter).replaceData(it)
        }
    }

    @BindingAdapter("movieList")
    @JvmStatic
    fun setMovieList(recyclerView: RecyclerView, dataList: PagedList<MovieWithGenres>?) {
        dataList?.let {
            if (it.size != 0)
                (recyclerView.adapter as MovieItemAdapter).apply {
                    submitList(it)
                    notifyDataSetChanged()
                }

        }
    }

    @BindingAdapter("isLoading")
    @JvmStatic
    fun setIsLoading(shimmerFrameLayout: ShimmerFrameLayout, isLoading: Boolean) {

        if (isLoading) {
            shimmerFrameLayout.startShimmer()
            return
        }
        shimmerFrameLayout.stopShimmer()
    }
}