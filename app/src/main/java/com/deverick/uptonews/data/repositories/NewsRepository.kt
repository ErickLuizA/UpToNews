package com.deverick.uptonews.data.repositories

import com.deverick.uptonews.data.api.CurrentsApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NewsRepository {
    suspend fun getRecommendedNews(
        language: String,
        category: String
    ): Flow<Response<CurrentsApiResponse>>
}