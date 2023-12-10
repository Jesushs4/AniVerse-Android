package com.example.aniverse.ui.listDetail

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
import com.example.aniverse.R
import com.example.aniverse.databinding.FragmentAnimeListBinding
import com.example.aniverse.databinding.FragmentListDetailBinding
import com.example.aniverse.ui.detail.AnimeDetailFragmentArgs
import com.example.aniverse.ui.list.AnimeListAdapter
import com.example.aniverse.ui.list.AnimeListFragmentDirections
import com.example.aniverse.ui.list.AnimeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListDetailFragment : Fragment() {
    private val args: ListDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentListDetailBinding
    private val viewModel: ListDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AnimeListAdapter(requireContext()) { anime ->
            val action = AnimeListFragmentDirections.actionAnimeListFragmentToAnimeDetailFragment(anime.mal_id)
            findNavController().navigate(action)
        }

        val rv = binding.listDetail
        rv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchAnimeListByListId(args.id)
                viewModel.uiState.collect {
                    adapter.submitList(it.anime)
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