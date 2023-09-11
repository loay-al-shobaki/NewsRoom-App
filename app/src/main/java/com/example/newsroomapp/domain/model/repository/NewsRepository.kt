package com.example.newsroomapp.domain.model.repository

import com.example.newsroomapp.domain.model.Article
import com.example.newsroomapp.util.Resource

interface NewsRepository {

    suspend fun getTopHeadlines(
        category: String
    ): Resource<List<Article>>


    suspend fun searchForNews(
        category: String
    ): Resource<List<Article>>

}