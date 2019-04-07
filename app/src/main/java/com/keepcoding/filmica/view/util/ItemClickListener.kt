package com.keepcoding.filmica.view.util

import com.keepcoding.filmica.data.Film

interface ItemClickListener {
    fun onItemClicked(film: Film)
}