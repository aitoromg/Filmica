package com.keepcoding.filmica.view.search

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.Editable
import android.text.TextWatcher
import com.keepcoding.filmica.R
import com.keepcoding.filmica.data.Film
import com.keepcoding.filmica.data.SearchRepo
import com.keepcoding.filmica.view.util.ItemOffsetDecoration
import com.keepcoding.filmica.view.util.ItemClickListener
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_noresults.*


const val MIN_SEARCH_QUERY = 3

class SearchFragment : Fragment() {

    lateinit var listener: ItemClickListener
    private var searching: Boolean = false

    val list: RecyclerView by lazy {
        val instance = view!!.findViewById<RecyclerView>(R.id.list_search)
        instance.addItemDecoration(ItemOffsetDecoration(R.dimen.offset_grid))
        instance.setHasFixedSize(true)
        instance
    }

    val adapter: SearchAdapter by lazy {
        val instance = SearchAdapter { film ->
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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.adapter = adapter

        textQuery.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    val query = text.toString()
                    if (query.length > MIN_SEARCH_QUERY) {
                        searching = false
                        progress.visibility = View.VISIBLE
                        layoutError.visibility = View.INVISIBLE
                        layoutNoResults.visibility = View.INVISIBLE
                        list.visibility = View.INVISIBLE
                        search(query)
                    } else {
                        searching = true
                        progress.visibility = View.INVISIBLE
                        layoutError.visibility = View.INVISIBLE
                        layoutNoResults.visibility = View.INVISIBLE
                        list.visibility = View.INVISIBLE
                    }
                }
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, after: Int, count: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

    }

    fun search(query: String) {
        SearchRepo.searchFilms(context!!, query, { films ->
            if (!searching && films.size > 0) {
                progress.visibility = View.INVISIBLE
                layoutError.visibility = View.INVISIBLE
                layoutNoResults.visibility = View.INVISIBLE
                list.visibility = View.VISIBLE
                adapter.setFilms(films)
            } else if (films.size == 0) {
                progress.visibility = View.INVISIBLE
                layoutError.visibility = View.INVISIBLE
                layoutNoResults.visibility = View.VISIBLE
                list.visibility = View.INVISIBLE
            }
        }, { error ->
            progress.visibility = View.INVISIBLE
            layoutError.visibility = View.VISIBLE
            layoutNoResults.visibility = View.INVISIBLE
            list.visibility = View.INVISIBLE

            error.printStackTrace()
        })
    }

    interface OnItemClickListener {
        fun onItemClicked(film: Film)
    }

}