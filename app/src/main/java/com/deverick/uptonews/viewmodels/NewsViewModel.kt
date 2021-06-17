package com.deverick.uptonews.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deverick.uptonews.R
import com.deverick.uptonews.data.api.CurrentsApiResponse
import com.deverick.uptonews.data.repositories.NewsRepository
import com.deverick.uptonews.models.News
import com.deverick.uptonews.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _news = MutableStateFlow<Resource<List<News>>>(Resource.Loading())
    val news: StateFlow<Resource<List<News>>> = _news

    init {
        getLatestNews(Locale.getDefault().language)
    }

    private fun getLatestNews(language: String) = viewModelScope.launch {
        _news.value = Resource.Loading()

        try {
            newsRepository.getLatestNews(language).collect { response ->
                val result = handleGetLatestNews(response)

                _news.value = result
            }
        } catch (e: Exception) {
            _news.value = Resource.Error(getStringResource(R.string.unexpected_error))
        }
    }

    private fun handleGetLatestNews(response: Response<CurrentsApiResponse>): Resource<List<News>> {
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.status == "error") {
                    return Resource.Error(getStringResource(R.string.error_loading_news))
                }

                return Resource.Success(res.news)
            }
        }

        return Resource.Error(response.message())
    }

    private fun getStringResource(id: Int): String {
        return getApplication<Application>().getString(id)
    }
}