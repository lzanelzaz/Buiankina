package ru.lzanelzaz.tinkofffintech.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FilmInfoDao {
    @Query("SELECT * FROM FilmInfo ORDER BY id DESC")
    suspend fun getFavourites(): List<FilmInfo>

    @Insert
    suspend fun insertFavourite(filmInfo: FilmInfo)

    @Query("DELETE FROM FilmInfo WHERE id = :kinopoiskId")
    suspend fun deleteFavourite(kinopoiskId: Int)
}