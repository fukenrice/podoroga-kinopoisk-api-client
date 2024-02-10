package com.example.kinopoisk_api_client.data.repository

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskRepository {
    @GET("films/top")
    suspend fun getTopFilms(@Query("type") type: String = "TOP_100_POPULAR_FILMS")

    @GET("films/{id}")
    suspend fun getFilm(@Path("id") id: Int)
}