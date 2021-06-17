package com.deverick.uptonews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.deverick.uptonews.R
import com.deverick.uptonews.UpToNewsApplication
import com.deverick.uptonews.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = auth.currentUser

        Glide
            .with(requireContext())
            .load(user?.photoUrl)
            .placeholder(R.drawable.ic_baseline_person_24)
            .error(R.drawable.ic_baseline_person_24)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.userAvatar)

        binding.userName.text = user?.displayName

        binding.displayLanguage.setOnClickListener {
            val popupMenu = PopupMenu(this.activity, binding.displayLanguage)

            popupMenu.menuInflater.inflate(R.menu.display_language_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.english -> {
                        lifecycleScope.launch {
                            UpToNewsApplication.setCustomLocale("en")

                            activity?.recreate()
                        }
                    }

                    R.id.portuguese -> {
                        lifecycleScope.launch {
                            UpToNewsApplication.setCustomLocale("pt")

                            activity?.recreate()
                        }
                    }
                }

                true
            }

            popupMenu.show()
        }

        binding.displayTheme.setOnClickListener { }
        binding.favorites.setOnClickListener { }
        binding.history.setOnClickListener { }
    }
}