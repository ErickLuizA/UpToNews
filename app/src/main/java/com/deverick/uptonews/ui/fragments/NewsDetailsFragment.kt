package com.deverick.uptonews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.deverick.uptonews.R
import com.deverick.uptonews.databinding.FragmentNewsDetailsBinding
import com.deverick.uptonews.viewmodels.DetailsViewModel
import com.deverick.uptonews.viewmodels.FavoriteResult
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsDetailsFragment : Fragment() {
    private lateinit var binding: FragmentNewsDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()

    private val arguments: NewsDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.webView.loadUrl(arguments.url)

        viewModel.isFavorite.observe(viewLifecycleOwner, { isFavorite ->
            if (isFavorite) {
                binding.fab.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
            } else {
                binding.fab.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
            }
        })

        binding.fab.setOnClickListener {
            lifecycleScope.launch {
                when (viewModel.toggleFavoriteNews(arguments.url)) {
                    FavoriteResult.Add -> {
                        Snackbar.make(it, "News added to favorites", Snackbar.LENGTH_SHORT).show()
                    }

                    FavoriteResult.Remove -> {
                        Snackbar.make(it, "News removed from favorites", Snackbar.LENGTH_SHORT)
                            .show()
                    }

                    FavoriteResult.AddFail -> {
                        Snackbar.make(
                            it,
                            "Failure in adding news to favorites",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    FavoriteResult.RemoveFail -> {
                        Snackbar.make(
                            it,
                            "Failure in removing news from favorites",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}