package ru.lzanelzaz.tinkofffintech.api

import javax.inject.Singleton
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import ru.lzanelzaz.tinkofffintech.BuildConfig
import ru.lzanelzaz.tinkofffintech.model.Description
import ru.lzanelzaz.tinkofffintech.model.Response

@Singleton
interface ApiService {
    @Headers("X-API-KEY: ${BuildConfig.API_KEY}")
    @GET("api/v2.2/films/top?type=TOP_100_POPULAR_FILMS")
    suspend fun getPopular(): Response

    @Headers("X-API-KEY: ${BuildConfig.API_KEY}")
    @GET("api/v2.2/films/{id}")
    suspend fun getDescription(@Path("id") id: String): Description
}