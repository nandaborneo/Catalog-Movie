package com.example.catalogmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogmovie.R
import com.example.catalogmovie.data.local.entity.GenreEntity
import com.example.catalogmovie.databinding.GenreMovieItemBinding

class GenreMovieItemAdapter(
    private var genreList: MutableList<GenreEntity>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GenreItemHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.genre_movie_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GenreItemHolder).bind(genreList[position])
    }

    override fun getItemCount(): Int = if(genreList.size > 4) 4 else genreList.size

    fun replaceData(genreList: MutableList<GenreEntity>) {
        setList(genreList)
    }

    private fun setList(genreList: MutableList<GenreEntity>) {
        this.genreList = genreList
        notifyDataSetChanged()
    }

    class GenreItemHolder(private var binding: GenreMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: GenreEntity) {
            binding.genre = genre
            binding.executePendingBindings()
        }
    }
}