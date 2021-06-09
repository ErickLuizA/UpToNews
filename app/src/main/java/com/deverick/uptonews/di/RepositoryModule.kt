package com.deverick.uptonews.di

import com.deverick.uptonews.repositories.NewsRepository
import com.deverick.uptonews.repositories.NewsRepositoryImpl
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
    fun provideNewsRepository(): NewsRepository {
        return NewsRepositoryImpl()
    }
}