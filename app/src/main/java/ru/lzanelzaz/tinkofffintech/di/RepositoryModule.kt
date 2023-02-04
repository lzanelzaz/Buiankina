package ru.lzanelzaz.tinkofffintech.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.lzanelzaz.tinkofffintech.AppRepository
import ru.lzanelzaz.tinkofffintech.api.ApiService

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAppRepository(
        apiService: ApiService
    ): AppRepository = AppRepository(apiService = apiService)

    @Provides
    @Singleton
    fun provideApiService(): ApiService = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech")
        .addConverterFactory(MoshiConverterFactory.create())
        .build().create(ApiService::class.java)

}