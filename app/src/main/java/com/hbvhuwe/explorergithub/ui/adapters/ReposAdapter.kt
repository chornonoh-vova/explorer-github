package com.hbvhuwe.explorergithub.ui.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.model.Repo
import com.hbvhuwe.explorergithub.ui.RepoActivity

class ReposAdapter(dataset: List<Repo>)
    : BaseAdapter<Repo, ReposAdapter.ViewHolder>(dataset, R.layout.repo_layout) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
            ViewHolder(LayoutInflater.from(p0.context).inflate(layout, p0, false))

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var repo: Repo
        var fullName: TextView = itemView.findViewById(R.id.repository_name)
        var description: TextView = itemView.findViewById(R.id.repo_description)
        var stars: TextView = itemView.findViewById(R.id.repo_stars)
        var language: TextView = itemView.findViewById(R.id.repo_language)

        init {
            itemView.setOnClickListener {
                val intent = Intent(it.context, RepoActivity::class.java).apply {
                    putExtra(Const.REPO_NAME_KEY, repo.name)
                    putExtra(Const.REPO_OWNER_KEY, repo.owner.login)
                    putExtra(Const.REPO_PATH_KEY, "/")
                }
                it.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.repo = dataset[position]
        holder.fullName.text = dataset[position].fullName
        holder.description.text = dataset[position].description
        holder.stars.text = "${dataset[position].starsCount}"
        holder.language.text = dataset[position].language
    }
}