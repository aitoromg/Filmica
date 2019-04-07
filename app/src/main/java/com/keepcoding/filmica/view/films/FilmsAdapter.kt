package com.keepcoding.filmica.view.films

import android.arch.paging.PagedListAdapter
import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keepcoding.filmica.R
import com.keepcoding.filmica.data.Film
import com.keepcoding.filmica.view.util.BaseFilmAdapter
import com.keepcoding.filmica.view.util.BaseFilmHolder
import com.keepcoding.filmica.view.util.SimpleTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_film.view.*

class FilmsAdapter(clickListener: ((Film) -> Unit)? = null) :
    PagedListAdapter<Film, FilmsAdapter.FilmViewHolder>(
        filmDiffCallback
    ) {

    private val clickListener = clickListener

    companion object {
        val filmDiffCallback = object : DiffUtil.ItemCallback<Film>() {
            override fun areContentsTheSame(p0: Film, p1: Film): Boolean {
                return p0 == p1
            }

            override fun areItemsTheSame(p0: Film, p1: Film): Boolean {
                return p0?.id == p1?.id
            }
        }
    }

    override fun onCreateViewHolder(recyclerView: ViewGroup, viewType: Int): FilmViewHolder {
        val itemView = LayoutInflater.from(recyclerView.context).inflate(R.layout.item_film, recyclerView, false)

        return FilmViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = getItem(position)
        film?.let {
            holder.bindFilm(it)
        }
    }

    class FilmViewHolder(
        view: View,
        clickListener: ((Film) -> Unit)? = null
    ) : BaseFilmHolder(view, clickListener) {

        override fun bindFilm(film: Film) {
            super.bindFilm(film)

            with(itemView) {
                labelTitle.text = film.title
                titleGenre.text = film.genre
                labelVotes.text = film.voteRating.toString()
                loadImage()
            }
        }

        private fun loadImage() {
            val target = SimpleTarget(
                successCallback = { bitmap, from ->
                    itemView.imgPoster.setImageBitmap(bitmap)
                    setColorFrom(bitmap)
                }
            )

            itemView.imgPoster.tag = target

            Picasso.get()
                .load(film.getPosterUrl())
                .error(R.drawable.placeholder)
                .into(target)
        }

        private fun setColorFrom(bitmap: Bitmap) {
            Palette.from(bitmap).generate { palette ->
                val defaultColor = ContextCompat.getColor(itemView.context, R.color.colorPrimary)
                val swatch = palette?.vibrantSwatch ?: palette?.dominantSwatch
                val color = swatch?.rgb ?: defaultColor

                itemView.container.setBackgroundColor(color)
                itemView.containerData.setBackgroundColor(color)
            }
        }
    }
}