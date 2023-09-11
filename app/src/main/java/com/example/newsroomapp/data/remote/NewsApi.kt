package com.example.newsroomapp.data.remote

import com.example.newsroomapp.domain.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    //https://newsapi.org/v2/top-headlines?country=us&apiKey=6f91aa64a213416faa49bf3c56f62f94

    @GET("top-headlines")
    suspend fun getBreakingNews(
        @Query("category") catgory: String,
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

    @GET("everything")
    suspend fun searchForNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
        const val API_KEY = "6f91aa64a213416faa49bf3c56f62f94"
    }
}