package com.example.kinopoisk_api_client.di

import com.example.kinopoisk_api_client.data.model.Country
import com.example.kinopoisk_api_client.data.model.Film
import com.example.kinopoisk_api_client.data.model.FilmListItem
import com.example.kinopoisk_api_client.data.model.Genre
import com.example.kinopoisk_api_client.data.model.ListResponse
import com.example.kinopoisk_api_client.data.repository.KinopoiskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkingModule::class]
)
class FakeNetworkingModule {
    @Provides
    fun provideKinopoiskRepo(): KinopoiskRepository = runBlocking {
        val repo =
            Mockito.mock(KinopoiskRepository::class.java)

        val films =
            listOf(
                FilmListItem(
                    filmId = 0,
                    nameRu = "first",
                    year = "2002",
                    genres = listOf(Genre("genre")),
                    posterUrlPreview = "https://eskipaper.com/images/cute-cat-1.jpg"
                ),
                FilmListItem(
                    filmId = 2,
                    nameRu = "second",
                    year = "2024",
                    genres = listOf(Genre("genre")),
                    posterUrlPreview = "https://eskipaper.com/images/cute-cat-1.jpg"
                ),
            )
        Mockito.`when`(repo.getTopFilms()).thenReturn(ListResponse(films))
        Mockito.`when`(repo.getFilm(0)).thenReturn(
            Film(
                kinopoiskId = 0,
                posterUrl = "https://eskipaper.com/images/cute-cat-1.jpg",
                nameRu = "first",
                description = "desc",
                genres = listOf(Genre("genre")),
                countries = listOf(Country("country"))
            )
        )
        return@runBlocking repo
    }
}
