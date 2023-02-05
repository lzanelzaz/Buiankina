package ru.lzanelzaz.tinkofffintech.favourites

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
import ru.lzanelzaz.tinkofffintech.databinding.FragmentScreenBinding

typealias Loaded = FavouritesViewModel.State.Loaded

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    private val viewModel: FavouritesViewModel by viewModels()
    private lateinit var binding: FragmentScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.title = resources.getString(R.string.favourites)
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
                override fun onItemClick(filmId: Int) {
                    viewModel.onItemClicked(filmId)
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