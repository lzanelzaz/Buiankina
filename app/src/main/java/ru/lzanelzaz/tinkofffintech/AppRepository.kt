package ru.lzanelzaz.tinkofffintech

import javax.inject.Inject
import javax.inject.Singleton
import ru.lzanelzaz.tinkofffintech.api.ApiService
import ru.lzanelzaz.tinkofffintech.db.FilmInfo
import ru.lzanelzaz.tinkofffintech.db.FilmInfoDao
import ru.lzanelzaz.tinkofffintech.model.Description

@Singleton
class AppRepository @Inject constructor(
    private val apiService: ApiService, private val filmInfoDao: FilmInfoDao
) {
    suspend fun getPopular() = apiService.getPopular()
    suspend fun getDescription(id: Int): Description = apiService.getDescription(id)

    suspend fun getFavourites(): List<Description> =
        filmInfoDao.getFavourites().map { it.description }

    suspend fun insertFavourite(description: Description) =
        filmInfoDao.insertFavourite(FilmInfo(description.kinopoiskId, description))

    suspend fun deleteFavourite(kinopoiskId: Int) =
        filmInfoDao.deleteFavourite(kinopoiskId)
}