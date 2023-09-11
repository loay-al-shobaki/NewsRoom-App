package com.example.newsroomapp.ui.theme.news_screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsroomapp.domain.model.Article
import com.example.newsroomapp.domain.model.repository.NewsRepository
import com.example.newsroomapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsScreenViewmodel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var seachJob: Job? = null

    var state by mutableStateOf(NewsScreenState())

    fun onEvent(event: NewsScreenEvent) {
        when (event) {
            is NewsScreenEvent.OnCloseIconClicked -> {
                state = state.copy(isSearchBarVisible = false, articles = emptyList())
                getNewsArticales(category = state.category)
            }

            is NewsScreenEvent.OnSerchIconClicked -> {
                state = state.copy(isSearchBarVisible = true, articles = emptyList())
            }

            is NewsScreenEvent.onCategoryChange -> {
                state = state.copy(category = event.category)
                getNewsArticales(state.category)
            }

            is NewsScreenEvent.onNewsCardClicked -> {
                state = state.copy(selectedArticle = event.article)
            }

            is NewsScreenEvent.OnSearchQueryChanged -> {
                state = state.copy(searchQuery = event.searchQuery)
                seachJob?.cancel()
                seachJob = viewModelScope.launch {
                    delay(1000)
                    serchForNews(query = state.searchQuery)
                }

            }

            else -> {}
        }
    }


    private fun getNewsArticales(category: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = newsRepository.getTopHeadlines(category = category)
            Log.d("ahmed", "getNewsArticales: ${result.data}")
            when (result) {
                is Resource.Success -> {
                    state = state.copy(
                        articles = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        articles = emptyList()
                    )
                }
            }
        }
    }


    private fun serchForNews(query: String) {
        if (query.isEmpty()) {
            return
        }
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = newsRepository.searchForNews(query)
            Log.d("ahmed", "getNewsArticales: ${result.data}")
            when (result) {
                is Resource.Success -> {
                    state = state.copy(
                        articles = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        articles = emptyList()
                    )
                }
            }
        }
    }
}

