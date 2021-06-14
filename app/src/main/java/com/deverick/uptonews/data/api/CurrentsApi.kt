package com.deverick.uptonews.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentsApi {
    @GET("/v1/latest-news")
    suspend fun getLatestNews(
        @Query("language")
        language: String,
    ): Response<CurrentsApiResponse>

    @GET("/v1/search")
    suspend fun searchNewsByCategory(
        @Query("language")
        language: String,
        @Query("category")
        category: String,
    ): Response<CurrentsApiResponse>

    @GET("/v1/search")
    suspend fun searchNewsByKeyword(
        @Query("language")
        language: String,
        @Query("keywords")
        keyword: String,
    ): Response<CurrentsApiResponse>

    @GET("/v1/available/categories")
    suspend fun getAvailableCategories(): Response<CurrentsApiAvailableResponse>
}