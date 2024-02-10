package com.example.kinopoisk_api_client.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
class DispatcherModule {
    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO
}