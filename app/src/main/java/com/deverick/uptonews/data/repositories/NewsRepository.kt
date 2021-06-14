package com.deverick.uptonews.data.repositories

import com.deverick.uptonews.data.api.CurrentsApiAvailableResponse
import com.deverick.uptonews.data.api.CurrentsApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NewsRepository {
    suspend fun getLatestNews(
        language: String,
    ): Flow<Response<CurrentsApiResponse>>

    suspend fun getAvailableCategories(): Flow<Response<CurrentsApiAvailableResponse>>
}