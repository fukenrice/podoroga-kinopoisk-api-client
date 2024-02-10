package com.example.kinopoisk_api_client.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.kinopoisk_api_client.MainCoroutineScopeRule
import com.example.kinopoisk_api_client.data.model.Film
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


class FilmDetailsViewModelTest {
    private lateinit var viewModel: FilmDetailsViewModel
    private lateinit var repository: KinopoiskRepository
    private val film = Film(
        kinopoiskId = 1,
        posterUrl = "dsf",
        nameRu = "name",
        description = "desc",
        genres = listOf(),
        countries = listOf()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineScope =  MainCoroutineScopeRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() = runTest {
        repository = Mockito.mock(KinopoiskRepository::class.java)
        viewModel = FilmDetailsViewModel(0, repository, Dispatchers.Main)
    }

    @Test
    fun getFilmSuccess() = runTest {
        Mockito.`when`(repository.getFilm(0)).thenReturn(film)
        viewModel.getFilm()
        val expected = Film(
            kinopoiskId = 1,
            posterUrl = "dsf",
            nameRu = "name",
            description = "desc",
            genres = listOf(),
            countries = listOf()
        )
        assertEquals(expected, viewModel.film.value.data)
    }

    @Test
    fun getFilmSError() = runTest {
        Mockito.`when`(repository.getFilm(0)).thenThrow(Error())
        viewModel.getFilm()
        assertEquals("Ошибка получения данных, проверьте подключение к сети", viewModel.film.value.message)

    }

    @Test
    fun isEmpty() {
        assertTrue(viewModel.isEmpty())
    }

    @Test
    fun isNotEmpty() = runTest {
        Mockito.`when`(repository.getFilm(0)).thenReturn(film)
        viewModel.getFilm()
        assertFalse(viewModel.isEmpty())
    }
}