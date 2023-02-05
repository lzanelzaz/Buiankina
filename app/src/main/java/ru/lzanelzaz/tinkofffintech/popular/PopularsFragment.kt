package ru.lzanelzaz.tinkofffintech.popular

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
import ru.lzanelzaz.tinkofffintech.databinding.FragmentPopularsBinding

typealias Loaded = PopularsViewModel.State.Loaded
typealias Loading = PopularsViewModel.State.Loading
typealias Error = PopularsViewModel.State.Error

@AndroidEntryPoint
class PopularsFragment : Fragment() {
    private val viewModel: PopularsViewModel by viewModels()
    private lateinit var binding: FragmentPopularsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPopularsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindToViewModel()
    }

    private fun bindToViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.recyclerView.visibility =
                if (state is PopularsViewModel.State.Loaded) View.VISIBLE else View.INVISIBLE

            val adapter = PopularsListAdapter()
            val dataset = if (state is PopularsViewModel.State.Loaded)
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

            with(binding.stateViewLayout) {
                // Error/ loading view
                stateView.visibility =
                    if (state is Loaded) View.GONE else View.VISIBLE
                val imageRes =
                    if (state is Loading) R.drawable.loading_animation else R.drawable.network
                statusImageView.setImageResource(imageRes)

                errorTextView.visibility =
                    if (state is Error) View.VISIBLE else View.GONE

                retryButton.visibility =
                    if (state is Error) View.VISIBLE else View.GONE

                retryButton.setOnClickListener { viewModel.onRetryButtonPressed() }

                binding.bottomNavigation.favouritesButton.setOnClickListener {
                    it.findNavController()
                        .navigate(R.id.action_popularsFragment_to_favouritesFragment)
                }
            }
        }
    }
}