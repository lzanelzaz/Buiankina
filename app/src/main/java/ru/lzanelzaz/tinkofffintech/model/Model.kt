package ru.lzanelzaz.tinkofffintech.model

data class Film(
    val filmId: Int,
    val nameRu: String,
    val posterUrl: String,
    val year: String,
    val genres: List<Genre>,
    var isFavourite: Boolean = false
)

data class Response(val films: List<Film>)

data class Genre(val genre: String)
data class Country(val country: String)

data class Description(
    val kinopoiskId: Int,
    val year: String,
    val nameRu: String,
    val posterUrl: String,
    var posterDrawable: String = "",
    val genres: List<Genre>,
    val countries: List<Country>,
    val description: String,
    var isFavourite: Boolean = false
)