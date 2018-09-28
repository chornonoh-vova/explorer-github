package com.hbvhuwe.explorergithub.ui.adapters

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH: RecyclerView.ViewHolder>(
        dataset: List<T>,
        @LayoutRes val layout: Int
): androidx.recyclerview.widget.RecyclerView.Adapter<VH>() {
    var dataset = dataset
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    final override fun getItemCount() = dataset.size

//    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
//            vhFactory(LayoutInflater.from(parent.context).inflate(layout, parent, false))
}