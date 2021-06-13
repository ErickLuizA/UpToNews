package com.deverick.uptonews.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deverick.uptonews.data.api.CurrentsApiResponse
import com.deverick.uptonews.data.repositories.NewsRepository
import com.deverick.uptonews.models.News
import com.deverick.uptonews.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _news = MutableStateFlow<Resource<List<News>>>(Resource.Loading())
    val news: StateFlow<Resource<List<News>>> = _news

    init {
        viewModelScope.launch {
            getLatestNews("en")
        }
    }

    @InternalCoroutinesApi
    suspend fun getLatestNews(language: String) {
        try {
            newsRepository.getLatestNews(language).collect { response ->
                val result = handleGetLatestNews(response)

                _news.value = result
            }
        } catch (e: Exception) {
            _news.value = Resource.Error("A unexpected error happened")
        }
    }

    private fun handleGetLatestNews(response: Response<CurrentsApiResponse>): Resource<List<News>> {
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.status == "error") {
                    return Resource.Error("Error loading the news")
                }

                return Resource.Success(res.news)
            }
        }

        return Resource.Error(response.message())
    }
}