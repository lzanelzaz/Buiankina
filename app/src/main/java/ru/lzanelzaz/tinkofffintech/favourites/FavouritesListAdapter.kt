package ru.lzanelzaz.tinkofffintech.favourites

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
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
                val openFileInput = context.openFileInput(item.posterDrawable)
                itemPoster.setImageBitmap(BitmapFactory.decodeStream(openFileInput))
                openFileInput.close()
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
                        File(context.filesDir, "${item.kinopoiskId}.jpeg").delete()
                        listener.onItemRemoveClick(item.kinopoiskId)
                        star.visibility = View.INVISIBLE
                        item.isFavourite = false
                    } else {
                        val uri = "${item.kinopoiskId}.jpeg"
                        val outputStream = File(context.filesDir, uri).outputStream()
                        itemPoster.drawable.toBitmap(itemPoster.width, itemPoster.height)
                            .compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.close()
                        listener.onItemClick(item.kinopoiskId, uri)
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