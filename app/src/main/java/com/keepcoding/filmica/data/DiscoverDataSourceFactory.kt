package com.keepcoding.filmica.data

import android.arch.paging.DataSource
import android.content.Context

class DiscoverDataSourceFactory(
        context: Context
): DataSource.Factory<Int, Film>() {
    val context = context

    override fun create(): DataSource<Int, Film> {
        return DiscoverDataSource(context)
    }
}