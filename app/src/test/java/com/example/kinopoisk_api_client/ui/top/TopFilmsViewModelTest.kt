package com.example.kinopoisk_api_client.ui.top

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.kinopoisk_api_client.MainCoroutineScopeRule
import com.example.kinopoisk_api_client.data.model.FilmListItem
import com.example.kinopoisk_api_client.data.model.Genre
import com.example.kinopoisk_api_client.data.model.ListResponse
import com.example.kinopoisk_api_client.data.repository.KinopoiskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class TopFilmsViewModelTest {
    private lateinit var viewModel: TopFilmsViewModel
    private lateinit var repository: KinopoiskRepository
    private val films = listOf<FilmListItem>(
        FilmListItem(
            filmId = 0,
            nameRu = "first",
            year = "2002",
            genre = listOf(Genre("genre")),
            posterUrlPreview = ""
        ),
        FilmListItem(
            filmId = 2,
            nameRu = "second",
            year = "2024",
            genre = listOf(Genre("genre")),
            posterUrlPreview = ""
        ),
    )
    private val response = ListResponse(films)

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineScope =  MainCoroutineScopeRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() = runTest {
        repository = Mockito.mock(KinopoiskRepository::class.java)
        viewModel = TopFilmsViewModel(repository, Dispatchers.Main)
    }

    @Test
    fun getFilmsSuccess() = runTest {
        `when`(repository.getTopFilms()).thenReturn(response)
        viewModel.getFilms()
        val expected = listOf(
            FilmListItem(
                filmId = 0,
                nameRu = "first",
                year = "2002",
                genre = listOf(Genre("genre")),
                posterUrlPreview = ""
            ),
            FilmListItem(
                filmId = 2,
                nameRu = "second",
                year = "2024",
                genre = listOf(Genre("genre")),
                posterUrlPreview = ""
            ),
        )

        assertEquals(expected, viewModel.films.value.data)
    }

    @Test
    fun getFilmsError() = runTest {
        `when`(repository.getTopFilms()).thenThrow(Error())
        viewModel.getFilms()
        assertEquals("Ошибка получения данных, проверьте подключение к сети", viewModel.films.value.message)
    }

    @Test
    fun isEmpty() {
        assertTrue(viewModel.isEmpty())
    }

    @Test
    fun isNottEmpty() = runTest {
        `when`(repository.getTopFilms()).thenReturn(response)
        viewModel.getFilms()
        assertFalse(viewModel.isEmpty())
    }

}
