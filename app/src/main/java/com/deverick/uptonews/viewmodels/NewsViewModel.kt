package com.deverick.uptonews.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deverick.uptonews.models.News
import com.deverick.uptonews.models.NewsResponse
import com.deverick.uptonews.repositories.NewsRepository
import com.deverick.uptonews.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _news = MutableStateFlow<Resource<NewsResponse>>(Resource.Loading())

    val news: StateFlow<Resource<NewsResponse>> = _news

    init {
        viewModelScope.launch {
            newsRepository.getRecommendedNews().collect { recommendedNews ->
                _news.value = Resource.Success(recommendedNews)
            }
        }
    }
}