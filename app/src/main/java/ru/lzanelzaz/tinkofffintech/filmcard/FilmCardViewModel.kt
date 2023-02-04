package ru.lzanelzaz.tinkofffintech.filmcard

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

    var filmId: String = ""
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
                val description = appRepository.getDescription(filmId)
                _state.value = State.Loaded(description)
            } catch (exception: Exception) {
                _state.value = State.Error
            }

        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val description: Description) : State
        object Error : State
    }
}