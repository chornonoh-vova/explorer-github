package com.hbvhuwe.explorergithub.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.models.GitHubRepo

class RepositoriesAdapter(private val dataset: Array<GitHubRepo>):
        RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fullName: TextView = itemView.findViewById(R.id.repo_full_name)
        var description: TextView = itemView.findViewById(R.id.repo_description)
        var stars: TextView = itemView.findViewById(R.id.repo_stars)
        var language: TextView = itemView.findViewById(R.id.repo_language)
        init {
            itemView.setOnClickListener {
                Toast.makeText(it.context, "${fullName.text} clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoriesAdapter.ViewHolder{
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.repo_layout, parent, false))
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fullName.text = dataset[position].fullName
        holder.description.text = dataset[position].description
        holder.stars.text = "${dataset[position].starsCount}"
        holder.language.text = dataset[position].language
    }
}