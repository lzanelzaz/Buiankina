package ru.lzanelzaz.tinkofffintech.filmcard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.lzanelzaz.tinkofffintech.AppRepository
import ru.lzanelzaz.tinkofffintech.model.Description

@HiltViewModel
class FilmCardViewModel @Inject constructor(private val appRepository: AppRepository) :
    ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    var filmId: Int = 0
        set(value) {
            field = value
            loadState()
        }

    fun onRetryButtonPressed() {
        loadState()
    }

    private fun loadState() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val description = appRepository.checkId(filmId)
                _state.value = if (description == null)
                    State.Loaded(appRepository.getDescription(filmId))
                else State.LoadedFromDb(description)
            } catch (exception: Exception) {
                _state.value = State.Error
                Log.i("fdggf", exception.toString())
            }

        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val description: Description) : State
        data class LoadedFromDb(val description: Description) : State
        object Error : State
    }
}