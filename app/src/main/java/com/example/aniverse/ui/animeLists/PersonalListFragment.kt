package com.example.aniverse.ui.animeLists

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.aniverse.R
import com.example.aniverse.data.database.AnimeDBRepository
import com.example.aniverse.data.database.ListEntity
import com.example.aniverse.databinding.FragmentAnimeListBinding
import com.example.aniverse.databinding.FragmentPersonalListBinding
import com.example.aniverse.databinding.PopupFormBinding
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
    lateinit var repository: AnimeDBRepository

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
            showFormPopup()
        }
        val adapter = PersonalListAdapter(requireContext())
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

    private fun showFormPopup() {
        // Inflating the popup layout using View Binding
        val popupBinding = PopupFormBinding.inflate(layoutInflater)

        val popupWindow = PopupWindow(
            popupBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            isFocusable = true
            showAtLocation(binding.root, Gravity.CENTER, 0, 0)
        }

        popupBinding.buttonCreateList.setOnClickListener {
            val listName = popupBinding.editTextListName.text.toString()
            createList(listName)
            popupWindow.dismiss()
        }
    }
    private fun createList(name: String) {
        val newList = ListEntity(name = name)
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertList(newList)
        }
    }
}