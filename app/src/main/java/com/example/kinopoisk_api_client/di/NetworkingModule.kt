package com.example.kinopoisk_api_client.di

import com.example.kinopoisk_api_client.data.repository.KinopoiskRepository
import com.example.kinopoisk_api_client.utils.secrets.API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {
    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val apiKeyInterceptor = Interceptor {
            val request = it.request().newBuilder().addHeader("x-api-key", API_KEY).build()
            return@Interceptor it.proceed(request)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
        val gson = GsonConverterFactory.create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kinopoiskapiunofficial.tech/api/v2.2/")
            .client(client)
            .addConverterFactory(gson)
            .build()
        return retrofit
    }

    @Provides
    fun provideKinopoiskRepo(retrofit: Retrofit): KinopoiskRepository {
        return retrofit.create(KinopoiskRepository::class.java)
    }
}