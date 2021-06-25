package com.deverick.uptonews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deverick.uptonews.R
import com.deverick.uptonews.databinding.FragmentFavoritesBinding
import com.deverick.uptonews.ui.adapters.NewsAdapter
import com.deverick.uptonews.utils.Resource
import com.deverick.uptonews.viewmodels.FavoritesViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var newsAdapter: NewsAdapter

    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.title = getString(R.string.favorites)

        setUpRecyclerView()

        if (auth.currentUser != null) {
            lifecycleScope.launch {
                viewModel.getFavorites(auth.currentUser!!.uid).collect { resource ->
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
        }

        newsAdapter.setOnClickListener {
            findNavController().navigate(
                FavoritesFragmentDirections.actionFavoritesFragmentToNewsDetailsFragment(it)
            )
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

        binding.favoriteNewsRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}