package com.keepcoding.filmica.data

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

object DiscoverRepo {

    private val films: MutableList<Film> = mutableListOf()

    fun discoverFilms(
        context: Context,
        page: Int,
        callbackSuccess: ((MutableList<Film>, Int) -> Unit),
        callbackError: ((VolleyError) -> Unit)
    ) {
        requestDiscoverFilms(callbackSuccess, callbackError, context, page)
    }

    private fun requestDiscoverFilms(
        callbackSuccess: (MutableList<Film>, Int) -> Unit,
        callbackError: (VolleyError) -> Unit,
        context: Context,
        page: Int
    ) {
        val url = ApiRoutes.discoverUrl(page)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val newFilms = Film.parseFilms(response)
                FilmsRepo.addNewFilm(newFilms)
                callbackSuccess.invoke(newFilms, response.getInt("total_pages"))
            },
            { error ->
                callbackError.invoke(error)
            })

        Volley.newRequestQueue(context)
            .add(request)
    }

}