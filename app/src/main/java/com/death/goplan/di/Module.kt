package com.death.goplan.di

import android.content.Context
import com.death.goplan.data.repository.TripRepo
import com.death.goplan.data.repository.TripRepoImpl
import com.death.goplan.network.TripService
import com.death.goplan.network.mainHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTripRepo(tripService: TripService): TripRepo = TripRepoImpl(tripService)
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("MainClient")
    fun provideMainClient(): HttpClient {
        return mainHttpClient
    }

    @Provides
    @Singleton
    fun provideUserApiInterface(@Named("MainClient") httpClient: HttpClient): TripService {
        return TripService(httpClient)
    }
}