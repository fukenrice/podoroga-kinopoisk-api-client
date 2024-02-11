package com.example.kinopoisk_api_client.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk_api_client.data.model.Film
import com.example.kinopoisk_api_client.data.repository.KinopoiskRepository
import com.example.kinopoisk_api_client.ui.top.TAG
import com.example.kinopoisk_api_client.utils.resource.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FilmDetailsViewModel @AssistedInject constructor(
    @Assisted private val id: Int,
    private val repository: KinopoiskRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _film = MutableStateFlow<Resource<Film>>(Resource.loading(null))
    val film: StateFlow<Resource<Film>> = _film

    fun getFilm() {
        viewModelScope.launch(context = dispatcher) {
            try {
                _film.value = Resource.loading(_film.value.data)
                val response = repository.getFilm(id)
                _film.value = Resource.success(response)
            } catch (e: Throwable) {
                _film.value = Resource.error("Ошибка получения данных, проверьте подключение к сети", null)
                Log.d(TAG, "getFilms: " + e.message)
            }
        }
    }

    fun isEmpty() = _film.value.data == null

    @AssistedFactory
    interface Factory {
        fun create(id: Int): FilmDetailsViewModel
    }
}