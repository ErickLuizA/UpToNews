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
            getRecommended("en", "general")
        }
    }

    @InternalCoroutinesApi
    suspend fun getRecommended(language: String, category: String) {
        newsRepository.getRecommendedNews(language, category).collect { response ->
            val result = handleGetRecommended(response)

            _news.value = result
        }
    }

    private fun handleGetRecommended(response: Response<CurrentsApiResponse>): Resource<List<News>> {
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.status == "error") {
                    return Resource.Error("Api error")
                }

                return Resource.Success(res.news)
            }
        }

        return Resource.Error(response.message())
    }
}