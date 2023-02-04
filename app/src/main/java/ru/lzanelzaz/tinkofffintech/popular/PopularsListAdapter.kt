package ru.lzanelzaz.tinkofffintech.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.lzanelzaz.tinkofffintech.databinding.FilmItemBinding
import ru.lzanelzaz.tinkofffintech.model.Film

class PopularsListAdapter :
    ListAdapter<Film, PopularsListAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(FilmItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        /*holder.itemView.setOnClickListener { view ->
            view.findNavController()
                .navigate(
                    R.id.action_listRepositoriesFragment_to_repositoryInfoFragment,
                    RepositoryInfoFragment.createArguments(repoName = item.name)
                )
        }*/
    }

    class ItemViewHolder(private val binding: FilmItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Film) {
            with(binding) {
                itemName.text = item.nameRu
                itemPoster.load(
                    item.posterUrlPreview.toUri().buildUpon().scheme("https").build()
                )
                itemGenreYear.text = item.genres[0].genre + " (" + item.year + ")"
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