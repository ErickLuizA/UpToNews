package com.deverick.uptonews

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.deverick.uptonews.databinding.ActivityMainBinding
import com.deverick.uptonews.ui.fragments.HomeFragment
import com.deverick.uptonews.ui.fragments.SearchFragment
import com.deverick.uptonews.ui.fragments.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val settingsFragment = SettingsFragment()

        var selectedFragment: Fragment = homeFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainerView, settingsFragment, "3")
            .hide(settingsFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainerView, searchFragment, "2")
            .hide(searchFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainerView, homeFragment, "1")
            .commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction().hide(selectedFragment)
                        .show(homeFragment)
                        .commit()

                    selectedFragment = homeFragment

                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction().hide(selectedFragment)
                        .show(searchFragment)
                        .commit()

                    selectedFragment = searchFragment
                }
                R.id.settingsFragment -> {
                    supportFragmentManager.beginTransaction().hide(selectedFragment)
                        .show(settingsFragment)
                        .commit()

                    selectedFragment = settingsFragment
                }
            }
            true
        }
    }
}
