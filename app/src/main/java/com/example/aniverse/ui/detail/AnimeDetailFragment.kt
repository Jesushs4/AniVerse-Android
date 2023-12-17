package com.example.aniverse.ui.detail

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                    binding.toolbar.title = it.title
                    binding.animeImage.load(it.image_url)
                    binding.animeName.text = it.title
                }
            }
        }

        if (!isInternetAvailable(requireContext())) {
            Snackbar.make(binding.root, getString(R.string.noInternet), Snackbar.LENGTH_LONG).show()
            binding.titleOpenings.text = ""
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.fetchOpenings(args.id)
                    viewModel.themeList.collect {
                        binding.openingsText.movementMethod = ScrollingMovementMethod()
                        binding.openingsText.text = it
                    }
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
                        ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, listNames)
                    val autoCompleteTextView = binding.autoCompleteTextViewLists
                    autoCompleteTextView.setAdapter(adapter)
                }
            }
        }

        var selectedListId: Int? = null

        binding.autoCompleteTextViewLists.setOnItemClickListener { parent, view, position, id ->
            selectedListId = allListEntities[position].id
        }


        var toolbar = binding.toolbar

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonAddToList.setOnClickListener {
            selectedListId?.let {
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        try {
                            repository.insertAnimeList(AnimeListEntity(listId = it, mal_id = args.id))
                            Snackbar.make(binding.root, getString(R.string.addedAnime), Snackbar.LENGTH_LONG).show()
                        } catch (e: SQLiteConstraintException) {
                            Snackbar.make(binding.root, getString(R.string.alreadyAdded), Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }



        }

    fun isInternetAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }

    }

