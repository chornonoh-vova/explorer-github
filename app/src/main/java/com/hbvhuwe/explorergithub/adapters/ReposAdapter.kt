package com.hbvhuwe.explorergithub.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.RepoActivity
import com.hbvhuwe.explorergithub.models.Repo

class ReposAdapter(private var dataset: List<Repo>) :
        RecyclerView.Adapter<ReposAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var repo: Repo
        var fullName: TextView = itemView.findViewById(R.id.repository_name)
        var description: TextView = itemView.findViewById(R.id.repo_description)
        var stars: TextView = itemView.findViewById(R.id.repo_stars)
        var language: TextView = itemView.findViewById(R.id.repo_language)

        init {
            itemView.setOnClickListener {
                val intent = Intent(it.context, RepoActivity::class.java).apply {
                    putExtra("repository", repo)
                    putExtra("path", "")
                }
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.repo_layout, parent, false))

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.repo = dataset[position]
        holder.fullName.text = dataset[position].fullName
        holder.description.text = dataset[position].description
        holder.stars.text = "${dataset[position].starsCount}"
        holder.language.text = dataset[position].language
    }

    fun setRepos(repos: List<Repo>) {
        dataset = repos
        notifyDataSetChanged()
    }
}