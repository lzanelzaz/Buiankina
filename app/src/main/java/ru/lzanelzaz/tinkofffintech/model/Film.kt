package ru.lzanelzaz.tinkofffintech.model

data class Film(
    val filmId: String,
    val nameRu: String,
    val posterUrlPreview: String,
    val year: String,
    val genres: List<Genre>
)

data class Response(val films: List<Film>)

data class Genre(val genre: String)

data class Description(
    val nameRu: String,
    val posterUrl: String,
    val genres: List<Genre>,
    val countries: List<Country>,
    val description: String
)

data class Country(val country: String)