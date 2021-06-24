package com.deverick.uptonews.di

import com.deverick.uptonews.data.api.CurrentsApi
import com.deverick.uptonews.data.repositories.FavoriteRepository
import com.deverick.uptonews.data.repositories.FavoriteRepositoryImpl
import com.deverick.uptonews.data.repositories.NewsRepository
import com.deverick.uptonews.data.repositories.NewsRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
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

    @Singleton
    @Provides
    fun provideFavoritesRepository(
        firestore: FirebaseFirestore
    ): FavoriteRepository {
        return FavoriteRepositoryImpl(
            firestore
        )
    }
}