package com.example.aniverse.ui.listDetail

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.aniverse.data.repository.Anime
import com.example.aniverse.databinding.AnimeListItemBinding
import com.example.aniverse.databinding.AnimePersonalListItemBinding

class ListDetailAdapter(private val context: Context,
                        private val onAnimeClicked: ((Anime) -> Unit)? = null,
                        private val onDeleteClicked: ((Int) -> Unit)? = null
                        ):ListAdapter<Anime, ListDetailAdapter.AnimeViewHolder>(AnimeDiffCallback) {

    inner class AnimeViewHolder(private val binding:AnimePersonalListItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(anime:Anime) {
            binding.animeImage.load(anime.image_url)
            binding.animeName.text = anime.title
            binding.deleteIcon.setOnClickListener {
                onDeleteClicked?.invoke(anime.mal_id)
            }
        }
    }

    private object AnimeDiffCallback:DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(oldItem: Anime, newItem: Anime) = oldItem.mal_id == newItem.mal_id
        override fun areContentsTheSame(oldItem: Anime, newItem: Anime) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder = AnimeViewHolder(AnimePersonalListItemBinding.inflate(
        LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = getItem(position)
        holder.bind(anime)
        holder.itemView.setOnClickListener {
            onAnimeClicked?.invoke(anime)
        }
    }


}