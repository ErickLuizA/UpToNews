package com.deverick.uptonews.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.deverick.uptonews.data.api.CurrentsApiAvailableResponse
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
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _categories = MutableStateFlow<Resource<List<String>>>(Resource.Loading())
    val categories: StateFlow<Resource<List<String>>>
        get() = _categories

    // Use LiveData because StateFlow was not working
    private val _searchedNews = MutableLiveData<Resource<List<News>>>()
    val searchedNews: LiveData<Resource<List<News>>>
        get() = _searchedNews

    init {
        getAvailableCategories()
    }

    private fun getAvailableCategories() = viewModelScope.launch {
        _categories.value = Resource.Loading()

        try {
            newsRepository.getAvailableCategories().collect { response ->
                val result = handleGetAvailableCategories(response)

                _categories.value = result
            }
        } catch (e: Exception) {
            _categories.value = Resource.Error("A unexpected error occurred")
        }
    }

    fun searchNewsByKeyword(
        language: String,
        keyword: String,
    ) = viewModelScope.launch {
        _searchedNews.value = Resource.Loading()

        try {
            newsRepository.searchNewsByKeyword(language, keyword).collect { response ->
                val result = handleSearchNews(response)

                _searchedNews.value = result
            }
        } catch (e: Exception) {
            _searchedNews.value = Resource.Error("A unexpected error occurred")
        }
    }

    fun searchNewsByCategory(
        language: String,
        category: String,
    ) = viewModelScope.launch {
        _searchedNews.value = Resource.Loading()

        try {
            newsRepository.searchNewsByCategory(language, category).collect { response ->
                val result = handleSearchNews(response)

                _searchedNews.value = result
            }
        } catch (e: Exception) {
            _searchedNews.value = Resource.Error("A unexpected error occurred")
        }
    }

    private fun handleGetAvailableCategories(response: Response<CurrentsApiAvailableResponse>): Resource<List<String>> {
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.status == "error") {
                    return Resource.Error("Error loading the news")
                }

                return Resource.Success(res.categories)
            }
        }

        return Resource.Error("Error while loading available categories")
    }

    private fun handleSearchNews(response: Response<CurrentsApiResponse>): Resource<List<News>> {
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.status == "error") {
                    return Resource.Error("Error loading the news")
                }

                return Resource.Success(res.news)
            }
        }

        return Resource.Error("Error while loading searched news")
    }
}