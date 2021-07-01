package com.deverick.uptonews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deverick.uptonews.R
import com.deverick.uptonews.databinding.FragmentHomeBinding
import com.deverick.uptonews.ui.adapters.NewsAdapter
import com.deverick.uptonews.utils.Resource
import com.deverick.uptonews.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsAdapter: NewsAdapter

    @InternalCoroutinesApi
    private val viewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()

        lifecycleScope.launch {
            viewModel.news.collect { resource ->
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
            }
        }

        newsAdapter.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToNewsDetailsFragment(it)
            )
        }

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.getLatestNews(Locale.getDefault().language, true).invokeOnCompletion {
                binding.swipeToRefresh.isRefreshing = false
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

        newsAdapter.setHasStableIds(true)

        binding.newsRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}