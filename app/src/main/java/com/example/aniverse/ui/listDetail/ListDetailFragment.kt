package com.example.aniverse.ui.listDetail

import android.app.AlertDialog
import android.content.Intent
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
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.databinding.FragmentAnimeListBinding
import com.example.aniverse.databinding.FragmentListDetailBinding
import com.example.aniverse.ui.detail.AnimeDetailFragmentArgs
import com.example.aniverse.ui.list.AnimeListAdapter
import com.example.aniverse.ui.list.AnimeListFragmentDirections
import com.example.aniverse.ui.list.AnimeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ListDetailFragment : Fragment() {
        private val args: ListDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentListDetailBinding
    private val viewModel: ListDetailViewModel by viewModels()

    @Inject
    lateinit var repository: AnimeRepository

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

        val adapter = ListDetailAdapter(requireContext(), onAnimeClicked = { anime ->
            val action = ListDetailFragmentDirections.actionListDetailFragmentToAnimeListDetailFragment(anime.mal_id)
            findNavController().navigate(action)
        },  onDeleteClicked = { animeId ->
            showDeleteConfirmationDialog(animeId)
        })

        val rv = binding.listDetail
        rv.adapter = adapter
        var animeNames = ""

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchAnimeListByListId(args.id)
                viewModel.uiState.collect {
                    adapter.submitList(it.anime)
                    animeNames = it.anime.joinToString(separator = "\n") { it.title }
                }
            }
        }

        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, animeNames)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        var toolbar = binding.toolbar

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }



    }

    private fun showDeleteConfirmationDialog(animeId: Int) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que quieres borrar este anime de la lista?")
            .setPositiveButton("Borrar") { dialog, _ ->
                deleteAnimeFromList(animeId)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            .create()
        dialog.show()
    }

    private fun deleteAnimeFromList(mal_id:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAnimeFromList(mal_id)
        }
    }



}