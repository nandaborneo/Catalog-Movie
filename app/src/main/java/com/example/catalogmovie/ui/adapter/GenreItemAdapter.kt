package com.example.catalogmovie.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogmovie.R
import com.example.catalogmovie.data.local.entity.GenreEntity
import com.example.catalogmovie.databinding.GenreItemBinding
import com.example.catalogmovie.ui.listener.ItemClickListener
import com.example.catalogmovie.ui.viewmodel.GenreViewModel

class GenreItemAdapter constructor(
    private var genreList: MutableList<GenreEntity>,
    private val viewModel: GenreViewModel,
    val context: Activity
) : PagedListAdapter<GenreEntity, GenreItemAdapter.GenreItemHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreItemHolder {
        return GenreItemHolder(
            context,
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.genre_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GenreItemHolder, position: Int) {
        getItem(position)?.apply {
            holder.bind(this, object : ItemClickListener {
                override fun onItemClick() {
                    genreId?.let {
                        viewModel.openMovieByGenre(it)
                    }
                }
            })
        }
    }

    class GenreItemHolder constructor(
        val context: Activity,
        val binding: GenreItemBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(genreItem: GenreEntity, mainActionListener: ItemClickListener){
            binding.action = mainActionListener
            binding.genre = genreItem
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GenreEntity>() {
            override fun areItemsTheSame(oldItem: GenreEntity, newItem: GenreEntity): Boolean {
                return oldItem.genreId == newItem.genreId
            }
            override fun areContentsTheSame(oldItem: GenreEntity, newItem: GenreEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}