package com.deverick.uptonews.di

import com.deverick.uptonews.data.api.CurrentsApi
import com.deverick.uptonews.data.repositories.*
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

    @Singleton
    @Provides
    fun provideHistoryRepository(
        firestore: FirebaseFirestore
    ): HistoryRepository {
        return HistoryRepositoryImpl(
            firestore
        )
    }
}