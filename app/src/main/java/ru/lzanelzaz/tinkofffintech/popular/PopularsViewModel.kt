package ru.lzanelzaz.tinkofffintech.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.lzanelzaz.tinkofffintech.AppRepository
import ru.lzanelzaz.tinkofffintech.model.Film

@HiltViewModel
class PopularsViewModel @Inject constructor(private val appRepository: AppRepository) :
    ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    init {
        loadState()
    }

    fun onRetryButtonPressed() {
        loadState()
    }

    private fun loadState() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val films = appRepository.getPopular().films
                _state.value = State.Loaded(films)
            } catch (exception: Exception) {
                _state.value = State.Error
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val films: List<Film>) : State
        object Error : State
    }
}