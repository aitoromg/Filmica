package com.keepcoding.filmica.view.trending

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keepcoding.filmica.R
import com.keepcoding.filmica.data.Film
import com.keepcoding.filmica.data.TrendingRepo
import com.keepcoding.filmica.data.TrendingDataSourceFactory
import com.keepcoding.filmica.view.films.FilmsAdapter
import com.keepcoding.filmica.view.util.ItemOffsetDecoration
import com.keepcoding.filmica.view.util.ItemClickListener
import kotlinx.android.synthetic.main.fragment_trending.*
import kotlinx.android.synthetic.main.layout_error.*

const val PAGE_SIZE = 10

class TrendingFragment : Fragment() {

    lateinit var listener: ItemClickListener
    lateinit var trendingList: LiveData<PagedList<Film>>

    val list: RecyclerView by lazy {
        val instance = view!!.findViewById<RecyclerView>(R.id.list_trending)
        instance.addItemDecoration(ItemOffsetDecoration(R.dimen.offset_grid))
        instance.setHasFixedSize(true)
        instance
    }

    val adapter: FilmsAdapter by lazy {
        val instance = FilmsAdapter { film ->
            this.listener.onItemClicked(film)
        }

        instance
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ItemClickListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.adapter = adapter

        //btnRetry?.setOnClickListener { reload() }

        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

        val trendingDataSourceFactory = TrendingDataSourceFactory(context!!)

        trendingList = LivePagedListBuilder<Int, Film>(trendingDataSourceFactory, config).build()

        trendingList.observe(this, Observer { list ->
            adapter.submitList(list)
            progress.visibility = View.INVISIBLE
            list_trending.visibility = View.VISIBLE
        })
    }

    override fun onResume() {
        super.onResume()
        //this.reload()
    }

    /*fun reload() {
        TrendingRepo.trendingFilms(context!!,
            { films ->
                progress?.visibility = View.INVISIBLE
                layoutError?.visibility = View.INVISIBLE
                list.visibility = View.VISIBLE
                adapter.setFilms(films)
            },
            { error ->
                progress?.visibility = View.INVISIBLE
                list.visibility = View.INVISIBLE
                layoutError?.visibility = View.VISIBLE

                error.printStackTrace()
            })
    }*/

    interface OnItemClickListener {
        fun onItemClicked(film: Film)
    }

}