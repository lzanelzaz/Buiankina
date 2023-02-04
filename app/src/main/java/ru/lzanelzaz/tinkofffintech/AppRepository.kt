package ru.lzanelzaz.tinkofffintech

import javax.inject.Inject
import javax.inject.Singleton
import ru.lzanelzaz.tinkofffintech.api.ApiService

@Singleton
class AppRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getPopular() = apiService.getPopular()
    suspend fun getDescription(id: String) = apiService.getDescription(id)
}