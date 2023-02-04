package ru.lzanelzaz.tinkofffintech.popular

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.lzanelzaz.tinkofffintech.R
import ru.lzanelzaz.tinkofffintech.databinding.FilmItemBinding
import ru.lzanelzaz.tinkofffintech.filmcard.FilmCardFragment
import ru.lzanelzaz.tinkofffintech.model.Film

class PopularsListAdapter : ListAdapter<Film, PopularsListAdapter.ItemViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            FilmItemBinding.inflate(LayoutInflater.from(parent.context)), parent.context
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ItemViewHolder(private val binding: FilmItemBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Film) {
            with(binding) {
                itemName.text = item.nameRu
                itemPoster.load(
                    item.posterUrlPreview.toUri().buildUpon().scheme("https").build()
                )
                itemGenreYear.text = context.getString(
                    R.string.genreYear,
                    item.genres[0].genre.replaceFirstChar { it.uppercase() },
                    item.year
                )
                card.setOnClickListener { view ->
                    view.findNavController().navigate(
                            R.id.action_popularsFragment_to_filmCardFragment,
                            FilmCardFragment.createArguments(filmId = item.filmId)
                        )
                }
                card.setOnLongClickListener { view ->

                    true
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.nameRu == newItem.nameRu
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }
    }
}