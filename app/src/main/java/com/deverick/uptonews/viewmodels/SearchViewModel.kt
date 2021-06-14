package com.deverick.uptonews.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deverick.uptonews.data.api.CurrentsApiAvailableResponse
import com.deverick.uptonews.data.repositories.NewsRepository
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
    val categories: StateFlow<Resource<List<String>>> = _categories

    init {
        viewModelScope.launch {
            getAvailableCategories()
        }
    }

    private suspend fun getAvailableCategories() {
        try {
            newsRepository.getAvailableCategories().collect { response ->
                val result = handleGetAvailableCategories(response)

                _categories.value = result
            }
        } catch (e: Exception) {
            _categories.value = Resource.Error("A unexpected error occurred")
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
}