package com.example.newsroomapp.data.remote.repository

import android.util.Log
import com.example.newsroomapp.data.remote.NewsApi
import com.example.newsroomapp.domain.model.Article
import com.example.newsroomapp.domain.model.repository.NewsRepository
import com.example.newsroomapp.util.Resource

class NewsRepositoryImp(
    private val newsApi: NewsApi
) : NewsRepository {
    override suspend fun getTopHeadlines(category: String): Resource<List<Article>> {
        return try {
            val response = newsApi.getBreakingNews(catgory = category)

            Log.d("loay", "getTopHeadlines: $response")
            Resource.Success(response.articles)


        } catch (e: Exception) {
            Resource.Error(message = "Failed to fetch news ${e.message}")
        }

    }


    override suspend fun searchForNews(query: String): Resource<List<Article>> {
        return try {
            val response = newsApi.searchForNews(query = query)
            Resource.Success(response.articles)


        } catch (e: Exception) {
            Resource.Error(message = "Failed to fetch news ${e.message}")
        }
    }
}