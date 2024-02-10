package com.example.kinopoisk_api_client.ui.top

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk_api_client.data.model.FilmListItem
import com.example.kinopoisk_api_client.data.repository.KinopoiskRepository
import com.example.kinopoisk_api_client.utils.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "TopFilmsViewModel"

@HiltViewModel
class TopFilmsViewModel @Inject constructor(private val repository: KinopoiskRepository, private val dispatcher: CoroutineDispatcher) : ViewModel() {
    private val _films = MutableStateFlow<Resource<List<FilmListItem>>>(Resource.loading(listOf()))
    val films: StateFlow<Resource<List<FilmListItem>>> = _films

    fun getFilms() {
        viewModelScope.launch(context = dispatcher) {
            try {
                _films.value = Resource.loading(_films.value.data)
                val response = repository.getTopFilms()
                _films.value = Resource.success(response.films)
            } catch (e: Throwable) {
                _films.value = Resource.error("Ошибка получения данных, проверьте подключение к сети", listOf())
                Log.d(TAG, "getFilms: " + e.message)
            }
        }
    }
}