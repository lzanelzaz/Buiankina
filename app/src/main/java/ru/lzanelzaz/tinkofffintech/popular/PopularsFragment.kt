package ru.lzanelzaz.tinkofffintech.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.lzanelzaz.tinkofffintech.databinding.FragmentPopularsBinding

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
            binding.popularRecyclerView.adapter = getRecyclerViewAdapter(state)
        }
    }
}