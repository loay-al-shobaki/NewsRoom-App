package com.example.newsroomapp.di

import com.example.newsroomapp.data.remote.NewsApi
import com.example.newsroomapp.data.remote.NewsApi.Companion.BASE_URL
import com.example.newsroomapp.data.remote.repository.NewsRepositoryImp
import com.example.newsroomapp.domain.model.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApis(): NewsApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsReposiory(newsApi: NewsApi): NewsRepository {
        return NewsRepositoryImp(newsApi)
    }
}