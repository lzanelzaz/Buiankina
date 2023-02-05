package ru.lzanelzaz.tinkofffintech.popular

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import java.io.File
import ru.lzanelzaz.tinkofffintech.R
import ru.lzanelzaz.tinkofffintech.RecyclerClickListener
import ru.lzanelzaz.tinkofffintech.databinding.FilmItemBinding
import ru.lzanelzaz.tinkofffintech.filmcard.FilmCardFragment
import ru.lzanelzaz.tinkofffintech.model.Film
import ru.lzanelzaz.tinkofffintech.saveImage


class PopularsListAdapter :
    ListAdapter<Film, PopularsListAdapter.ItemViewHolder>(DiffCallback()) {

    private lateinit var listener: RecyclerClickListener
    fun setItemListener(listener: RecyclerClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            FilmItemBinding.inflate(LayoutInflater.from(parent.context)), parent.context
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener)
    }

    class ItemViewHolder(private val binding: FilmItemBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Film, listener: RecyclerClickListener) {
            with(binding) {
                itemPoster.load(
                    item.posterUrl.toUri().buildUpon().scheme("https").build()
                )
                itemName.text = item.nameRu
                itemGenreYear.text = context.getString(
                    R.string.genreYear,
                    item.genres[0].genre.replaceFirstChar { it.uppercase() },
                    item.year
                )
                star.visibility = if (item.isFavourite) View.VISIBLE else View.INVISIBLE

                card.setOnClickListener { view ->
                    view.findNavController().navigate(
                        R.id.action_popularsFragment_to_filmCardFragment,
                        FilmCardFragment.createArguments(filmId = item.filmId)
                    )
                }
                card.setOnLongClickListener {
                    val isFavourite = star.visibility == View.VISIBLE
                    if (isFavourite) {
                        File(
                            context.filesDir,
                            context.resources.getString(R.string.fileName, item.filmId)
                        ).delete()
                        listener.onItemRemoveClick(item.filmId)
                        star.visibility = View.INVISIBLE
                    } else {
                        saveImage(
                            context.resources.getString(R.string.fileName, item.filmId),
                            item.posterUrl,
                            context
                        )
                        listener.onItemClick(item.filmId)
                        star.visibility = View.VISIBLE
                    }
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