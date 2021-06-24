package com.deverick.uptonews.data.repositories

import com.deverick.uptonews.models.News
import com.deverick.uptonews.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun addFavorite(userId: String, news: News): Boolean

    fun removeFavorite(userId: String, news: News): Boolean

    suspend fun getFavorites(userId: String): Flow<Resource<List<News>>>
}