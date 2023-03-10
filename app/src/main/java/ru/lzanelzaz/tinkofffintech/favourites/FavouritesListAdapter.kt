package ru.lzanelzaz.tinkofffintech.favourites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import ru.lzanelzaz.tinkofffintech.R
import ru.lzanelzaz.tinkofffintech.RecyclerClickListener
import ru.lzanelzaz.tinkofffintech.databinding.FilmItemBinding
import ru.lzanelzaz.tinkofffintech.filmcard.FilmCardFragment
import ru.lzanelzaz.tinkofffintech.model.Description
import ru.lzanelzaz.tinkofffintech.saveImage

class FavouritesListAdapter :
    ListAdapter<Description, FavouritesListAdapter.ItemViewHolder>(DiffCallback()) {

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
        fun bind(item: Description, listener: RecyclerClickListener) {
            with(binding) {
                itemName.text = item.nameRu
                itemPoster.setImageURI(
                    File(
                        context.filesDir,
                        context.resources.getString(R.string.fileName, item.kinopoiskId)
                    ).toUri()
                )
                itemGenreYear.text = context.getString(
                    R.string.genreYear,
                    item.genres[0].genre.replaceFirstChar { it.uppercase() },
                    item.year
                )
                card.setOnClickListener { view ->
                    view.findNavController().navigate(
                        R.id.action_favouritesFragment_to_filmCardFragment,
                        FilmCardFragment.createArguments(filmId = item.kinopoiskId)
                    )
                }
                star.visibility = View.VISIBLE

                card.setOnLongClickListener {
                    if (item.isFavourite) {
                        File(
                            context.filesDir,
                            context.resources.getString(R.string.fileName, item.kinopoiskId)
                        ).delete()
                        listener.onItemRemoveClick(item.kinopoiskId)
                        star.visibility = View.INVISIBLE
                        item.isFavourite = false
                    } else {
                        saveImage(
                            context.resources.getString(R.string.fileName, item.kinopoiskId),
                            item.posterUrl,
                            context
                        )
                        listener.onItemClick(item.kinopoiskId)
                        star.visibility = View.VISIBLE
                        item.isFavourite = true
                    }
                    true
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Description>() {
        override fun areItemsTheSame(oldItem: Description, newItem: Description): Boolean {
            return oldItem.kinopoiskId == newItem.kinopoiskId
        }

        override fun areContentsTheSame(oldItem: Description, newItem: Description): Boolean {
            return oldItem == newItem
        }
    }
}