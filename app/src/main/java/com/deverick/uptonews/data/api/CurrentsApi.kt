package com.deverick.uptonews.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentsApi {
    @GET("/search")
    suspend fun searchNewsByCategory(
        @Query("language")
        language: String,
        @Query("category")
        category: String,
    ): Response<CurrentsApiResponse>

    @GET("/latest-news")
    suspend fun getLatestNews(
        @Query("language")
        language: String,
    ): Response<CurrentsApiResponse>
}