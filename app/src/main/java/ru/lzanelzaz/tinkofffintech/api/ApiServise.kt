package ru.lzanelzaz.tinkofffintech.api

import javax.inject.Singleton
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.lzanelzaz.tinkofffintech.BuildConfig
import ru.lzanelzaz.tinkofffintech.model.Response

@Singleton
interface ApiService {
    @Headers("X-API-KEY: ${BuildConfig.API_KEY}")
    @GET("api/v2.2/films/top?type=TOP_100_POPULAR_FILMS")
    suspend fun getPopular(): Response
}