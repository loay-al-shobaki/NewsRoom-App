package com.example.newsroomapp.ui.theme.news_screen

import com.example.newsroomapp.domain.model.Article

sealed class NewsScreenEvent {
    data class onNewsCardClicked(val article: Article) : NewsScreenEvent()
    data class onCategoryChange(val category: String):NewsScreenEvent()
    data class OnSearchQueryChanged(var searchQuery: String) : NewsScreenEvent()
    object OnSerchIconClicked:NewsScreenEvent()
    object OnCloseIconClicked:NewsScreenEvent()
}


