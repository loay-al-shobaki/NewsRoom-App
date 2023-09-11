package com.example.newsroomapp.domain.model


import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("publishedAt")
    val publishedAt: String? = null,
    @SerializedName("source")
    val source: Source? = null,
    @SerializedName("title")
    val title: String ,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("urlToImage")
    val urlToImage: String? = null
)