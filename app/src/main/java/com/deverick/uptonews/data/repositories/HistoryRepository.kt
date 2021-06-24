package com.deverick.uptonews.data.repositories

import com.deverick.uptonews.models.News
import com.deverick.uptonews.utils.Resource
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun addHistoryItem(userId: String, news: News)

    fun removeHistoryItem(userId: String, news: News): Boolean

    suspend fun getHistory(userId: String): Flow<Resource<List<News>>>
}