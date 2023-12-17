package com.example.aniverse.ui.animeLists

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.aniverse.R
import com.example.aniverse.data.database.AnimeDBRepository
import com.example.aniverse.data.database.ListEntity
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.databinding.FragmentAnimeListBinding
import com.example.aniverse.databinding.FragmentPersonalListBinding
import com.example.aniverse.ui.list.AnimeListAdapter
import com.example.aniverse.ui.list.AnimeListFragmentDirections
import com.example.aniverse.ui.list.AnimeListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PersonalListFragment : Fragment() {
    private lateinit var binding: FragmentPersonalListBinding
    private val viewModel: PersonalListViewModel by viewModels()
    @Inject
    lateinit var repository: AnimeRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOpenForm.setOnClickListener {
            showCreateDialog()
        }

        val adapter = PersonalListAdapter(requireContext(),onListClicked = { list ->
            val action = PersonalListFragmentDirections.actionPersonalListFragmentToListDetailFragment(list.id)
            findNavController().navigate(action)
        },
            onDeleteClicked = { listId ->
                showDeleteConfirmationDialog(listId)
            },
            onEditClicked = {listId ->
                    showEditDialog(listId)

            }
        )

        val rv = binding.personalList
        rv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    adapter.submitList(it.list)
                }
            }
        }


    }
    private fun showEditDialog(listId: Int) {
        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.editList))
            .setView(input)
            .setPositiveButton(getString(R.string.edit)) { dialog, _ ->
                val newName = input.text.toString()
                viewModel.editListName(listId, newName)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .create()
        dialog.show()
    }

    private fun showDeleteConfirmationDialog(listId: Int) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirmListDelete))
            .setMessage(getString(R.string.deleteList))
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                viewModel.deleteList(listId)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .create()
        dialog.show()
    }
    private fun showCreateDialog() {
        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.createList))
            .setView(input)
            .setPositiveButton(getString(R.string.create)) { dialog, _ ->
                val newName = input.text.toString()
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.createList(newName)
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.listCreated), Snackbar.LENGTH_LONG).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .create()
        dialog.show()
    }




}