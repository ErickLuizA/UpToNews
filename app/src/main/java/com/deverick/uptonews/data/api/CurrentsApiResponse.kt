package com.deverick.uptonews.data.api

import com.deverick.uptonews.models.News

data class CurrentsApiResponse(
    val status: String,
    val news: List<News>
)

data class CurrentsApiAvailableResponse(
    val status: String,
    val description: String,
    val regions: List<String>,
    val categories: List<String>,
    val languages: List<String>,
)