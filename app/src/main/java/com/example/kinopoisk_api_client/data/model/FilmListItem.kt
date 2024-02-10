package com.example.kinopoisk_api_client.data.model

import java.time.Year

data class FilmListItem(
    val filmId: Int,
    val nameRu: String,
    val year: String,
    val genre: List<Genre>,
    val posterUrlPreview: String
)

data class Genre(val genre: String)

