package com.keepcoding.filmica.data

import android.net.Uri
import com.keepcoding.filmica.BuildConfig

object ApiRoutes {

    fun discoverUrl(
        page: Int = 1
    ): String {
        return getUriBuilder()
            .appendPath("discover")
            .appendPath("movie")
            .appendQueryParameter("language", "en-US")
            .appendQueryParameter("sort_by", "popularity.desc")
            .appendQueryParameter("page", page.toString())
            .appendQueryParameter("include_adult", "false")
            .appendQueryParameter("include_video", "false")
            .build()
            .toString()
    }

    fun trendingUrl(
        page: Int = 1
    ): String {
        return getUriBuilder()
            .appendPath("trending")
            .appendPath("movie")
            .appendPath("day")
            .appendQueryParameter("language", "en-US")
            .appendQueryParameter("page", page.toString())
            .appendQueryParameter("include_adult", "false")
            .appendQueryParameter("include_video", "false")
            .build()
            .toString()
    }

    fun searchUrl(
        query: String,
        page: Int = 1
    ): String {
        return getUriBuilder()
            .appendPath("search")
            .appendPath("movie")
            .appendQueryParameter("query", query)
            .appendQueryParameter("language", "en-US")
            .appendQueryParameter("page", page.toString())
            .appendQueryParameter("include_adult", "false")
            .appendQueryParameter("include_video", "false")
            .build()
            .toString()
    }

    private fun getUriBuilder() =
        Uri.Builder()
            .scheme("https")
            .authority("api.themoviedb.org")
            .appendPath("3")
            .appendQueryParameter("api_key", BuildConfig.MovieDBApiKey)
}