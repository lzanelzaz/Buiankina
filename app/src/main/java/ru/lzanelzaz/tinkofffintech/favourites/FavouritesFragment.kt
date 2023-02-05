package ru.lzanelzaz.tinkofffintech.favourites

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lzanelzaz.tinkofffintech.R
import ru.lzanelzaz.tinkofffintech.RecyclerClickListener
import ru.lzanelzaz.tinkofffintech.databinding.FragmentFavouritesBinding

typealias Loaded = FavouritesViewModel.State.Loaded
typealias Loading = FavouritesViewModel.State.Loading
typealias Error = FavouritesViewModel.State.Error

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    private val viewModel: FavouritesViewModel by viewModels()
    private lateinit var binding: FragmentFavouritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindToViewModel()
    }

    private fun bindToViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.recyclerView.visibility =
                if (state is Loaded) View.VISIBLE else View.INVISIBLE

            val adapter = FavouritesListAdapter()
            val dataset = if (state is Loaded)
                state.films
            else
                emptyList()
            adapter.submitList(dataset)
            adapter.setItemListener(object : RecyclerClickListener {
                override fun onItemClick(filmId: Int, drawable: Drawable) {
                    viewModel.onItemClicked(filmId, drawable)
                }

                override fun onItemRemoveClick(filmId: Int) {
                    viewModel.onItemRemoveClicked(filmId)
                }
            })
            binding.recyclerView.adapter = adapter

            binding.bottomNavigation.popularsButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_favouritesFragment_to_popularsFragment)
            }
        }
    }
}