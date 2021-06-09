package com.deverick.uptonews.repositories

import com.deverick.uptonews.models.NewsResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getRecommendedNews(): Flow<NewsResponse>
}