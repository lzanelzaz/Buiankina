package ru.lzanelzaz.tinkofffintech.favourites

import android.graphics.drawable.Drawable
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
class FavouritesViewModel @Inject constructor(private val appRepository: AppRepository) :
    ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    init {
        loadState()
    }

    fun onItemRemoveClicked(filmId: Int) {
        viewModelScope.launch {
            try {
                appRepository.deleteFavourite(filmId)
            } catch (exception: Exception) {
                _state.value = State.Error
            }
        }
    }

    fun onItemClicked(filmId: Int, drawable: Drawable) {
        viewModelScope.launch {
            try {
                val description = appRepository.getDescription(filmId)
                //description.posterDrawable = drawable
                description.isFavourite = true
                appRepository.insertFavourite(description)
            } catch (exception: Exception) {
                _state.value = State.Error
            }
        }
    }

    private fun loadState() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val films = appRepository.getFavourites()
                _state.value = State.Loaded(films)
            } catch (exception: Exception) {
                _state.value = State.Error
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val films: List<Description>) : State
        object Error : State
    }
}