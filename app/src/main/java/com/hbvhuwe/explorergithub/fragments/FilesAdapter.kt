package com.hbvhuwe.explorergithub.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.models.GitHubFile

class FilesAdapter(private val dataset: Array<GitHubFile>): RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var fileName: TextView = itemView.findViewById(R.id.file_name) as TextView
        var fileIcon: ImageView = itemView.findViewById(R.id.file_icon) as ImageView
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.file_layout, parent, false))

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fileName.text = dataset[position].name
        if (dataset[position].type == "dir") {
            holder.fileIcon.setImageResource(R.mipmap.ic_folder)
        } else {
            holder.fileIcon.setImageResource(R.mipmap.ic_file)
        }
    }

}
