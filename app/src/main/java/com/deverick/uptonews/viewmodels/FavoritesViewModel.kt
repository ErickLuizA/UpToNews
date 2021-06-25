package com.deverick.uptonews.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.deverick.uptonews.data.repositories.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    application: Application
) : AndroidViewModel(application) {
    suspend fun getFavorites(userId: String) = favoriteRepository.getFavorites(userId)
}