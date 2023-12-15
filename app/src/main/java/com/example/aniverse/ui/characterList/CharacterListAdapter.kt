package com.example.aniverse.ui.characterList
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.aniverse.data.repository.Anime
import com.example.aniverse.data.repository.AnimeCharacter
import com.example.aniverse.databinding.AnimeListItemBinding
import com.example.aniverse.databinding.CharacterListItemBinding

class CharacterListAdapter(private val context: Context):ListAdapter<AnimeCharacter, CharacterListAdapter.CharacterViewHolder>(CharacterDiffCallback) {

    inner class CharacterViewHolder(private val binding: CharacterListItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(character:AnimeCharacter) {
            binding.characterImage.load(character.image_url)
            binding.characterName.text = character.name
        }
    }

    private object CharacterDiffCallback:DiffUtil.ItemCallback<AnimeCharacter>() {
        override fun areItemsTheSame(oldItem: AnimeCharacter, newItem: AnimeCharacter) = oldItem.mal_id == newItem.mal_id
        override fun areContentsTheSame(oldItem: AnimeCharacter, newItem: AnimeCharacter) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder = CharacterViewHolder(CharacterListItemBinding.inflate(
        LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
    }


}