package com.example.aniverse.ui.animeLists

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.aniverse.data.repository.Anime
import com.example.aniverse.data.repository.PersonalList
import com.example.aniverse.databinding.AnimeListItemBinding
import com.example.aniverse.databinding.PersonalListItemBinding

class PersonalListAdapter(private val context: Context,
                          private val onListClicked: ((PersonalList) -> Unit)? = null,
                          private val onDeleteClicked: ((Int) -> Unit)? = null,
                          private val onEditClicked: ((Int) -> Unit)? = null
                            ):ListAdapter<PersonalList, PersonalListAdapter.PersonalViewHolder>(PersonalDiffCallback) {

    inner class PersonalViewHolder(private val binding:PersonalListItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(list:PersonalList) {
            binding.listName.text = list.name
            Log.d("hola", list.toString())
            binding.deleteIcon.setOnClickListener {
                onDeleteClicked?.invoke(list.id)
                true
            }
            binding.editIcon.setOnClickListener{
                onEditClicked?.invoke(list.id)
            }
        }
    }

    private object PersonalDiffCallback:DiffUtil.ItemCallback<PersonalList>() {
        override fun areItemsTheSame(oldItem: PersonalList, newItem: PersonalList) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: PersonalList, newItem: PersonalList) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalViewHolder = PersonalViewHolder(PersonalListItemBinding.inflate(
        LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: PersonalViewHolder, position: Int) {
        val list = getItem(position)
        holder.bind(list)
        holder.itemView.setOnClickListener {
            onListClicked?.invoke(list)
        }
    }


}