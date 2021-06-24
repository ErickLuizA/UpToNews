package com.deverick.uptonews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deverick.uptonews.R
import com.deverick.uptonews.databinding.FragmentSearchBinding
import com.deverick.uptonews.ui.adapters.NewsAdapter
import com.deverick.uptonews.utils.Resource
import com.deverick.uptonews.viewmodels.SearchViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var newsAdapter: NewsAdapter

    private lateinit var locale: Locale

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)

        locale = Locale.getDefault()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()

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
                            getString(R.string.error_loading_categories),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }

                    is Resource.Success -> {
                        showCategoriesText()

                        resource.data?.forEach {
                            val chip = Chip(context)

                            chip.text = it

                            chip.setOnClickListener {
                                lifecycleScope.launch {
                                    viewModel.searchNewsByCategory(
                                        locale.language,
                                        chip.text.toString()
                                    )
                                }
                            }

                            binding.chipGroup.addView(chip)
                        }
                    }
                }
            }
        }

        viewModel.searchedNews.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideLoading()

                    resource.data?.let { response ->
                        newsAdapter.differ.submitList(response)
                    }
                }

                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Error -> {
                    hideLoading()

                    Snackbar.make(
                        view,
                        getString(R.string.error_loading_news),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        })

        newsAdapter.setOnClickListener {
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToNewsDetailsFragment(it)
            )
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
            lifecycleScope.launch {
                query?.let { keyword ->
                    viewModel.searchNewsByKeyword(locale.language, keyword)
                }
            }

            hideKeyboard()

            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    }

    private fun showLoading() {
        binding.rvLoadingIndicator.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.rvLoadingIndicator.visibility = View.INVISIBLE
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()

        newsAdapter.setHasStableIds(true)

        binding.searchNewsRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    fun hideKeyboard() =
        ViewCompat.getWindowInsetsController(requireView())?.hide(WindowInsetsCompat.Type.ime())
}