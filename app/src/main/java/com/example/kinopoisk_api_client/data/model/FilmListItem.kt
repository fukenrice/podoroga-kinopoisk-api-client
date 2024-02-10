package com.example.kinopoisk_api_client.data.model


data class FilmListItem(
    val filmId: Int,
    val nameRu: String,
    val year: String,
    val genres: List<Genre>,
    val posterUrlPreview: String
)

data class Genre(val genre: String)

