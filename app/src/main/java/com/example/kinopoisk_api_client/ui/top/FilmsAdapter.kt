package com.example.kinopoisk_api_client.ui.top

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoisk_api_client.R
import com.example.kinopoisk_api_client.data.model.FilmListItem
import com.example.kinopoisk_api_client.databinding.FilmListItemBinding
import com.squareup.picasso.Picasso

class FilmsAdapter(
    private val context: Context,
    private val onItemClicked: (film: FilmListItem) -> Unit,
    data: List<FilmListItem>
) : RecyclerView.Adapter<FilmsAdapter.DataViewHolder>() {
    private val data: MutableList<FilmListItem>
    private var displayedData: MutableList<FilmListItem>

    init {
        this.data = data.toMutableList()
        displayedData = data.toMutableList()
    }

    inner class DataViewHolder(
        private val onItemClicked: (film: FilmListItem) -> Unit,
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(film: FilmListItem) {
            val binding = FilmListItemBinding.bind(itemView)
            Picasso.get().load(film.posterUrlPreview).into(binding.ivPoster)
            binding.tvFilmName.text =
                if (film.nameRu.length <= 20) film.nameRu else film.nameRu.substring(0, 19) + "..."
            binding.tvFilmInfo.text =
                context.getString(
                    R.string.film_desc,
                    film.genres[0].genre.replaceFirstChar { it.uppercase() },
                    film.year
                )
            binding.root.setOnClickListener { onItemClicked(film) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            onItemClicked,
            LayoutInflater.from(parent.context).inflate(R.layout.film_list_item, parent, false)
        )

    override fun getItemCount() = displayedData.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(displayedData[position])
    }

    fun filter(query: String) {
        val result = data.filter {
            it.nameRu.lowercase().contains(query)
        }.toMutableList()
        displayedData = result
        notifyDataSetChanged()
    }

    fun clear() {
        displayedData.clear()
        data.clear()
    }

    fun addData(films: List<FilmListItem>) {
        displayedData.addAll(films)
        data.addAll(films)
    }
}