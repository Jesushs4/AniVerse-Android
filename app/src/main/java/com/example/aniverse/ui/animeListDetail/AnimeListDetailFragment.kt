package com.example.aniverse.ui.animeListDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.aniverse.R
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.databinding.FragmentAnimeListDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AnimeListDetailFragment : Fragment() {
    private val args: AnimeListDetailFragmentArgs by navArgs()
    @Inject
    lateinit var repository: AnimeRepository
    private lateinit var binding: FragmentAnimeListDetailBinding
    private val viewModel: AnimeListDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimeListDetailBinding.inflate(inflater, container, false)
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
                    binding.animeStatus.text = getString(R.string.status)+": "+ it.status
                    binding.animeEpisodes.text = getString(R.string.episodes)+": "+it.episodes
                }
            }
        }

        var toolbar = binding.toolbar

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }



    }
}