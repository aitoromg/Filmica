package com.keepcoding.filmica.view.search

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.View
import com.keepcoding.filmica.R
import com.keepcoding.filmica.data.Film
import com.keepcoding.filmica.view.util.BaseFilmAdapter
import com.keepcoding.filmica.view.util.BaseFilmHolder
import com.keepcoding.filmica.view.util.SimpleTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter(clickListener: ((Film) -> Unit)? = null) :
    BaseFilmAdapter<SearchAdapter.SearchViewHolder>(
        layoutItem = R.layout.item_search,
        holderCreator = { view -> SearchViewHolder(view, clickListener) }
    ) {


    class SearchViewHolder(
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