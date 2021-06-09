package com.deverick.uptonews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.deverick.uptonews.databinding.FragmentHomeBinding
import com.deverick.uptonews.ui.adapters.NewsAdapter
import com.deverick.uptonews.utils.Resource
import com.deverick.uptonews.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsAdapter: NewsAdapter

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()

        lifecycleScope.launch {
            viewModel.news.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        hideLoading()

                        resource.data?.let { response ->
                            newsAdapter.differ.submitList(response.articles)
                        }
                    }

                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()

                        Snackbar.make(view, "Error", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()

        binding.newsRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}