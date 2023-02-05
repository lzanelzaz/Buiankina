package ru.lzanelzaz.tinkofffintech.filmcard

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import ru.lzanelzaz.tinkofffintech.databinding.FragmentFilmCardBinding
import ru.lzanelzaz.tinkofffintech.model.Description

typealias Loaded = FilmCardViewModel.State.Loaded
typealias LoadedFromDb = FilmCardViewModel.State.LoadedFromDb
typealias Loading = FilmCardViewModel.State.Loading
typealias Error = FilmCardViewModel.State.Error

@AndroidEntryPoint
class FilmCardFragment : Fragment() {
    private val viewModel: FilmCardViewModel by viewModels()
    lateinit var binding: FragmentFilmCardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindToViewModel()
    }

    private fun bindToViewModel() {
        viewModel.filmId = requireNotNull(requireArguments().getInt(FILM_ID))
        viewModel.state.observe(viewLifecycleOwner) { state ->
            val description: Description? =
                when (state) {
                    is Loaded -> state.description
                    is LoadedFromDb -> state.description
                    else -> null
                }
            if (description != null) {
                with(binding) {
                    if (state is Loaded) {
                        descriptionPoster.load(
                            description.posterUrl.toUri().buildUpon().scheme("https").build()
                        )
                    } else {
                        val openFileInput =
                            requireContext().openFileInput(description.posterDrawable)
                        descriptionPoster.setImageBitmap(BitmapFactory.decodeStream(openFileInput))
                        openFileInput.close()
                    }
                    descriptionStory.text = description.description
                    descriptionName.text = description.nameRu
                    var genres: String = ""
                    description.genres.forEachIndexed { index, it ->
                        if (index != 0) genres += ", "
                        genres += it.genre
                    }
                    descriptionGenres.text = genres
                    var countries: String = ""
                    description.countries.forEachIndexed { index, it ->
                        if (index != 0) countries += ", "
                        countries += it.country
                    }
                    descriptionCountries.text = countries
                }
            }

            if (state is Loaded || state is LoadedFromDb) binding.genres.visibility =
                View.VISIBLE else View.GONE
            if (state is Loaded || state is LoadedFromDb) binding.countries.visibility =
                View.VISIBLE else View.GONE

            with(binding.stateViewLayout) {
                // Error/ loading view
                stateView.visibility = if (state !is Error) View.GONE else View.VISIBLE
                val imageRes =
                    if (state is Loading) ru.lzanelzaz.tinkofffintech.R.drawable.loading_animation else ru.lzanelzaz.tinkofffintech.R.drawable.network
                statusImageView.setImageResource(imageRes)

                errorTextView.visibility = if (state is Error) View.VISIBLE else View.GONE

                retryButton.visibility = if (state is Error) View.VISIBLE else View.GONE

                retryButton.setOnClickListener { viewModel.onRetryButtonPressed() }
            }
        }
    }

    companion object {
        private const val FILM_ID = "id"

        fun createArguments(filmId: Int): Bundle {
            return bundleOf(FILM_ID to filmId)
        }
    }
}