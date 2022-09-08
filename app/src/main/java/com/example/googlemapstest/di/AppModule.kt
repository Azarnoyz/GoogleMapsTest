package com.example.googlemapstest.di

import android.content.Context
import com.example.googlemapstest.ui.map.MapRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideMapRepository(@ApplicationContext appContext: Context): MapRepository =
        MapRepository(appContext)

}