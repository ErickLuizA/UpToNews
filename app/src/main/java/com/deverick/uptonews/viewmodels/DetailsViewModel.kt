package com.deverick.uptonews.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.deverick.uptonews.data.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class FavoriteResult { Add, Remove, AddFail, RemoveFail }

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    application: Application
) : AndroidViewModel(application) {

    private var _isFavorite = MutableLiveData<Boolean>(false)
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    suspend fun toggleFavoriteNews(url: String): FavoriteResult {
        if (_isFavorite.value == true) {

            _isFavorite.value = true

            return FavoriteResult.Add
        } else {

            _isFavorite.value = false

            return FavoriteResult.Remove
        }
    }

    private suspend fun getFavorites() {}
}