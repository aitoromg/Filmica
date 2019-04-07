package com.keepcoding.filmica.data

import android.arch.paging.PageKeyedDataSource
import android.content.Context

class TrendingDataSource(
        context: Context
): PageKeyedDataSource<Int, Film>() {
    private val context = context

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Film>) {
        TrendingRepo.trendingFilms(context, 1, { films, pages ->
            callback.onResult(films, null, if (pages > 1) 2 else null)
        }, { error ->
            error.printStackTrace()
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        TrendingRepo.trendingFilms(context, params.key, { films, pages ->
            callback.onResult(films, if (params.key < pages) params.key + 1 else null)
        }, { error ->
            error.printStackTrace()
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        TrendingRepo.trendingFilms(context, 1, { films, pages ->
            callback.onResult(films, if (params.key > 1) params.key - 1 else null)
        }, { error ->
            error.printStackTrace()
        })
    }

}