package com.deverick.uptonews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.deverick.uptonews.R
import com.deverick.uptonews.databinding.FragmentNewsDetailsBinding
import com.deverick.uptonews.viewmodels.DetailsViewModel
import com.deverick.uptonews.viewmodels.FavoriteResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsDetailsFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailsBinding
    private lateinit var auth: FirebaseAuth

    private val viewModel: DetailsViewModel by viewModels()
    private val arguments: NewsDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailsBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.title = null

        val user = auth.currentUser

        if (user != null) {
            viewModel.getFavorites(user.uid, arguments.news)
        }

        binding.webView.loadUrl(arguments.news.url!!)

        viewModel.isFavorite.observe(viewLifecycleOwner, { isFavorite ->
            if (isFavorite) {
                binding.fab.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                binding.fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        })

        binding.fab.setOnClickListener {
            lifecycleScope.launch {
                if (user != null) {
                    when (viewModel.toggleFavoriteNews(user.uid, arguments.news)) {
                        FavoriteResult.Add -> {
                            Snackbar.make(it, "News added to favorites", Snackbar.LENGTH_SHORT)
                                .show()
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
}