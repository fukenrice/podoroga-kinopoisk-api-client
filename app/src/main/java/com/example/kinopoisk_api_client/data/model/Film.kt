package com.example.kinopoisk_api_client.data.model

data class Film(
    val kinopoiskId: Int,
    val posterUrl: String,
    val nameRu: String,
    val description: String,
    val genres: List<Genre>,
    val countries: List<Country>
)

data class Country(val country: String)