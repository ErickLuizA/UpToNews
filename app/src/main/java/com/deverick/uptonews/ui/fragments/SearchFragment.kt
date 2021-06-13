package com.deverick.uptonews.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.deverick.uptonews.databinding.FragmentSearchBinding

const val TAG = "SearchFragment"

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchView.setOnQueryTextListener(queryTextListener)

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }
    }

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            Log.d(TAG, query ?: "")

            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            Log.d(TAG, newText ?: "")

            return true
        }
    }
}