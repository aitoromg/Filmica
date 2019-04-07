package com.keepcoding.filmica.data

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

object SearchRepo {

    private val films: MutableList<Film> = mutableListOf()

    fun searchFilms(
        context: Context,
        query: String,
        callbackSuccess: ((MutableList<Film>) -> Unit),
        callbackError: ((VolleyError) -> Unit)
        ) {
        if (films.isEmpty()) {
            requestSearchFilms(callbackSuccess, callbackError, context, query)
        } else {
            callbackSuccess.invoke(films)
        }
    }

    private fun requestSearchFilms(
        callbackSuccess: (MutableList<Film>) -> Unit,
        callbackError: (VolleyError) -> Unit,
        context: Context,
        query: String
    ) {
        val url = ApiRoutes.searchUrl(query)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val newFilms = Film.parseFilms(response)
                FilmsRepo.addNewFilm(newFilms)
                callbackSuccess.invoke(newFilms)
            },
            { error ->
                callbackError.invoke(error)
            })

        Volley.newRequestQueue(context)
            .add(request)
    }

}