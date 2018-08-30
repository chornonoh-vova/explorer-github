package com.hbvhuwe.explorergithub.ui.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.hbvhuwe.explorergithub.CircleTransform
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.model.User
import com.hbvhuwe.explorergithub.ui.UserActivity
import com.squareup.picasso.Picasso

class UsersAdapter(dataset: List<User>)
    : BaseAdapter<User, UsersAdapter.ViewHolder>(dataset, R.layout.user_layout,
        { UsersAdapter.ViewHolder(it) }) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userLogin: TextView = itemView.findViewById(R.id.user_login)
        val userAvatar: ImageView = itemView.findViewById(R.id.user_avatar)

        init {
            itemView.setOnClickListener {
                val intent = Intent(it.context, UserActivity::class.java).apply {
                    putExtra(Const.USER_KEY, userLogin.text)
                }
                it.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userLogin.text = dataset[position].login
        Picasso.get().load(dataset[position].avatarUrl.toString())
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.ic_account_circle)
                .transform(CircleTransform())
                .fit()
                .into(holder.userAvatar)
    }
}