package com.deverick.uptonews.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.deverick.uptonews.data.repositories.HistoryRepository
import com.deverick.uptonews.models.News
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    application: Application
) : AndroidViewModel(application) {

    suspend fun getHistory(userId: String) = historyRepository.getHistory(userId)

    fun removeHistoryItem(userId: String, news: News) =
        historyRepository.removeHistoryItem(userId, news)

    fun addHistoryItem(userId: String, news: News) =
        historyRepository.addHistoryItem(userId, news)
}