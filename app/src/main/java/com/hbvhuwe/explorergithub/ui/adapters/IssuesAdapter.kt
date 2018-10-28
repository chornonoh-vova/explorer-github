package com.hbvhuwe.explorergithub.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.model.Issue

class IssuesAdapter(dataset: List<Issue>)
    : BaseAdapter<Issue, IssuesAdapter.ViewHolder>(dataset, R.layout.issue_layout) {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var issue: Issue
        val issueStatusImage: ImageView = itemView.findViewById(R.id.issue_status_image)
        val issueTitle: TextView = itemView.findViewById(R.id.issue_title)
        val issueNumber: TextView = itemView.findViewById(R.id.issue_number)
        val issueTime: TextView = itemView.findViewById(R.id.issue_time)
        val issueLabels: ChipGroup = itemView.findViewById(R.id.issue_labels)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.issue = dataset[position]
        holder.issueTitle.text = holder.issue.title
        holder.issueNumber.text = "#${holder.issue.number}"
        if (holder.issue.state == "open") {
            holder.issueStatusImage.setImageResource(R.drawable.ic_issue_open)
            holder.issueTime.text = "Opened by ${holder.issue.user.login} at ${holder.issue.createdAt}"
        } else {
            holder.issueStatusImage.setImageResource(R.drawable.ic_issue_closed)
            holder.issueTime.text = "Closed by ${holder.issue.user.login} at ${holder.issue.closedAt}"
        }
        holder.issueLabels.removeAllViews()
        holder.issue.labels.forEach {
            val chip = Chip(holder.issueLabels.context)
            chip.text = it.name
            chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#${it.color}"))
            chip.isCheckable = false
            chip.isClickable = true
            holder.issueLabels.addView(chip)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
}