package com.example.aniverse.ui.animeLists

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
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
import androidx.navigation.fragment.findNavController
import com.example.aniverse.R
import com.example.aniverse.data.database.AnimeDBRepository
import com.example.aniverse.data.database.ListEntity
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.databinding.FragmentAnimeListBinding
import com.example.aniverse.databinding.FragmentPersonalListBinding
import com.example.aniverse.databinding.PopupFormBinding
import com.example.aniverse.ui.list.AnimeListAdapter
import com.example.aniverse.ui.list.AnimeListFragmentDirections
import com.example.aniverse.ui.list.AnimeListViewModel
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
                CoroutineScope(Dispatchers.IO).launch {
                    repository.deleteList(listId)
                }
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

        val dialog = AlertDialog.Builder(context)
            .setTitle("Editar nombre")
            .setView(input)
            .setPositiveButton("Editar") { dialog, _ ->
                val newName = input.text.toString()
                editListName(listId, newName)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
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

        val dialog = AlertDialog.Builder(context)
            .setTitle("Crear lista")
            .setView(input)
            .setPositiveButton("Crear") { dialog, _ ->
                val newName = input.text.toString()
                createList(newName)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            .create()
        dialog.show()
    }
    private fun createList(name: String) {
        val newList = ListEntity(name = name)
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertList(newList)
        }
    }
    private fun editListName(listId:Int, name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateListName(listId, name)
        }
    }
}