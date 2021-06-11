package com.deverick.uptonews.models

data class News(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val author: String,
    val image: String,
    val language: String,
    val category: List<String>,
    val published: String
)


