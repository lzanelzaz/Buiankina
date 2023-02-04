package ru.lzanelzaz.tinkofffintech.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.lzanelzaz.tinkofffintech.R
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

            binding.popularRecyclerView.visibility =
                if (state is PopularsViewModel.State.Loaded) View.VISIBLE else View.INVISIBLE

            binding.popularRecyclerView.adapter = getRecyclerViewAdapter(state)

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
            }
        }
    }
}