package com.deverick.uptonews.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.deverick.uptonews.databinding.FragmentSearchBinding
import com.deverick.uptonews.utils.Resource
import com.deverick.uptonews.viewmodels.SearchViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val TAG = "SearchFragment"

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewModel.categories.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        hideCategoriesText()
                    }

                    is Resource.Error -> {
                        hideCategoriesText()

                        Snackbar.make(
                            view,
                            resource.message ?: "Error loading available categories",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }

                    is Resource.Success -> {
                        showCategoriesText()

                        resource.data?.forEach {
                            val chip = Chip(context)

                            chip.text = it

                            binding.chipGroup.addView(chip)
                        }
                    }
                }
            }
        }

        binding.searchView.setOnQueryTextListener(queryTextListener)

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }
    }

    private fun hideCategoriesText() {
        binding.categoriesText.visibility = View.INVISIBLE
    }

    private fun showCategoriesText() {
        binding.categoriesText.visibility = View.VISIBLE
    }

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {


            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    }
}