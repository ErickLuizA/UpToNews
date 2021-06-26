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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deverick.uptonews.R
import com.deverick.uptonews.databinding.FragmentHistoryBinding
import com.deverick.uptonews.ui.adapters.NewsAdapter
import com.deverick.uptonews.utils.Resource
import com.deverick.uptonews.viewmodels.HistoryViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var newsAdapter: NewsAdapter

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.title = getString(R.string.history)

        setUpRecyclerView()

        val itemTouchHelper = ItemTouchHelper(callback)

        itemTouchHelper.attachToRecyclerView(binding.historyNewsRv)

        if (auth.currentUser != null) {
            lifecycleScope.launch {
                viewModel.getHistory(auth.currentUser!!.uid).collect { resource ->
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
                HistoryFragmentDirections.actionHistoryFragmentToNewsDetailsFragment(it)
            )
        }
    }

    private var callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val item = newsAdapter.differ.currentList[position]

            viewModel.removeHistoryItem(auth.currentUser!!.uid, item)

            Snackbar.make(requireView(), "News deleted from history", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel.addHistoryItem(auth.currentUser!!.uid, item)
                }

                show()
            }
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

        binding.historyNewsRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}