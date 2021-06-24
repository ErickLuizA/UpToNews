package com.deverick.uptonews.di

import com.deverick.uptonews.BuildConfig
import com.deverick.uptonews.data.api.CurrentsApi
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModuleModule {

    @Singleton
    @Provides
    fun provideCurrentsApi(): CurrentsApi {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor {
                val defaultRequest = it.request()

                val httpUrl = defaultRequest.url().newBuilder()
                    .addQueryParameter("apiKey", BuildConfig.CURRENTS_API_TOKEN)
                    .build()

                val requestBuilder = defaultRequest.newBuilder().url(httpUrl)

                it.proceed(requestBuilder.build())
            }

        val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.CURRENTS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        return builder.create(CurrentsApi::class.java)
    }

    @Singleton
    @Provides
    fun providesFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}