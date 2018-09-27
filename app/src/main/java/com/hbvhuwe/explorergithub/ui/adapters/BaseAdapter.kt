package com.hbvhuwe.explorergithub.ui.adapters

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseAdapter<T, VH: RecyclerView.ViewHolder>(
        dataset: List<T>,
        @LayoutRes val layout: Int
): RecyclerView.Adapter<VH>() {
    var dataset = dataset
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    final override fun getItemCount() = dataset.size

//    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
//            vhFactory(LayoutInflater.from(parent.context).inflate(layout, parent, false))
}