package com.deverick.uptonews.data.repositories

import com.deverick.uptonews.data.api.CurrentsApi
import com.deverick.uptonews.data.api.CurrentsApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val currentsApi: CurrentsApi,
) : NewsRepository {
    override suspend fun getRecommendedNews(
        language: String,
        categories: List<String>
    ): Flow<Response<CurrentsApiResponse>> {
        var category = ""

        categories.forEach {
            category = if (category.isNotEmpty()) {
                "$category,$it"
            } else {
                it
            }
        }

        val response = currentsApi.searchNewsByCategory(language, category)

        return flowOf(response)
    }
}