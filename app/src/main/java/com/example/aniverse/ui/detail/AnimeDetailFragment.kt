package com.example.aniverse.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.aniverse.R
import com.example.aniverse.data.database.AnimeDBRepository
import com.example.aniverse.data.database.AnimeListEntity
import com.example.aniverse.data.repository.AnimeList
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.data.repository.PersonalList
import com.example.aniverse.databinding.AnimeListItemBinding
import com.example.aniverse.databinding.FragmentAnimeDetailBinding
import com.example.aniverse.databinding.FragmentAnimeListBinding
import com.example.aniverse.ui.list.AnimeListAdapter
import com.example.aniverse.ui.list.AnimeListFragmentDirections
import com.example.aniverse.ui.list.AnimeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AnimeDetailFragment : Fragment() {
    private val args: AnimeDetailFragmentArgs by navArgs()
    @Inject
    lateinit var repository: AnimeRepository

    private var allListEntities: List<PersonalList> = emptyList()
    private lateinit var binding: FragmentAnimeDetailBinding
    private val viewModel: AnimeDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetch(args.id)
                viewModel.uiState.collect {
                    binding.animeImage.load(it.image_url)
                    binding.animeName.text = it.title
                }

            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchListNames()
                viewModel.listEntities.collect { listEntities ->
                    allListEntities = listEntities
                    val listNames = listEntities.map {it.name}
                    val adapter =
                        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listNames)
                    binding.spinnerLists.adapter = adapter
                }
            }

        }

        var selectedListId: Int? = null

        binding.spinnerLists.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedListId = allListEntities[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        var toolbar = binding.toolbar

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonAddToList.setOnClickListener {
            if (selectedListId != null) {
                val newAnimeList = AnimeListEntity(listId = selectedListId!!, animeId = args.id)
                CoroutineScope(Dispatchers.IO).launch {
                    repository.insertAnimeList(newAnimeList)
                    Log.d("anime añadido", newAnimeList.toString())
                }
                Log.d("HOLA", selectedListId.toString())
            }
        }

    }
}