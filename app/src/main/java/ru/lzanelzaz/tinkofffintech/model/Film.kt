package ru.lzanelzaz.tinkofffintech.model

data class Film(
    val nameRu: String,
    val year: String,
    val genres: List<Genre>,
    val posterUrlPreview: String
)

data class Response(val films: List<Film>)

data class Genre(val genre: String)