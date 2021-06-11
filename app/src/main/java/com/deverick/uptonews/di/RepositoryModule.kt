package com.deverick.uptonews.di

import com.deverick.uptonews.data.api.CurrentsApi
import com.deverick.uptonews.data.repositories.NewsRepository
import com.deverick.uptonews.data.repositories.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(
        currentsApi: CurrentsApi,
    ): NewsRepository {
        return NewsRepositoryImpl(
            currentsApi
        )
    }
}