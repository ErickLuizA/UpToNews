package com.deverick.uptonews.data.api

import com.deverick.uptonews.models.News

data class CurrentsApiResponse(
    val status: String,
    val news: List<News>
)
