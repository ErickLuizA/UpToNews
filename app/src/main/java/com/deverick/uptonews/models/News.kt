package com.deverick.uptonews.models

data class News(
    val id: String?,
    val title: String,
    val image: String?,
    val description: String,
    val date: String,
    val readTime: String?,
)
