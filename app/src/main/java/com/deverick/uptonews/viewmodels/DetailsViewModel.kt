package com.deverick.uptonews.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.deverick.uptonews.data.repositories.FavoriteRepository
import com.deverick.uptonews.data.repositories.HistoryRepository
import com.deverick.uptonews.models.News
import com.deverick.uptonews.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val historyRepository: HistoryRepository,
    application: Application
) : AndroidViewModel(application) {

    private var _isFavorite = MutableLiveData(false)
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    fun getFavorites(userId: String, news: News) = viewModelScope.launch {
        favoriteRepository
            .getFavorites(userId)
            .collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val isContained = resource.data?.contains(news)

                        _isFavorite.postValue(isContained)
                    }

                    else -> {
                        _isFavorite.postValue(false)
                    }
                }
            }
    }

    fun toggleFavoriteNews(userId: String, news: News): FavoriteResult {
        if (_isFavorite.value == true) {

            val result = favoriteRepository.removeFavorite(userId, news)

            return if (result) {
                _isFavorite.value = false

                FavoriteResult.Remove
            } else {
                _isFavorite.value = true

                FavoriteResult.RemoveFail
            }
        } else {

            val result = favoriteRepository.addFavorite(userId, news)

            return if (result) {
                _isFavorite.value = true

                FavoriteResult.Add
            } else {
                _isFavorite.value = false

                FavoriteResult.AddFail
            }
        }
    }

    fun addToHistory(userId: String, news: News) {
        historyRepository.addHistoryItem(userId, news)
    }
}