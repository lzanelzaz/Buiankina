package ru.lzanelzaz.tinkofffintech.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.lzanelzaz.tinkofffintech.AppRepository
import ru.lzanelzaz.tinkofffintech.api.ApiService
import ru.lzanelzaz.tinkofffintech.db.AppDatabase
import ru.lzanelzaz.tinkofffintech.db.FilmInfoDao

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAppRepository(
        apiService: ApiService, filmInfoDao: FilmInfoDao
    ): AppRepository = AppRepository(apiService = apiService, filmInfoDao = filmInfoDao)

    @Provides
    @Singleton
    fun provideApiService(): ApiService = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech")
        .addConverterFactory(MoshiConverterFactory.create())
        .build().create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "appDatabase"
        ).build()

    @Provides
    @Singleton
    fun provideBinDao(appDatabase: AppDatabase): FilmInfoDao = appDatabase.filmInfoDao()
}