package com.hbvhuwe.explorergithub.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.viewmodel.UserViewModel
import com.squareup.picasso.Picasso

class UserFragment : Fragment() {
    private lateinit var user: String
    private lateinit var login: TextView
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var publicRepos: TextView
    private lateinit var location: TextView
    private lateinit var avatar: ImageView

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        user = arguments!!.getString(Const.USER_KEY)
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avatar = view.findViewById(R.id.user_avatar)
        login = view.findViewById(R.id.user_login)
        name = view.findViewById(R.id.user_name)
        email = view.findViewById(R.id.user_email)
        location = view.findViewById(R.id.user_location)
        publicRepos = view.findViewById(R.id.user_public_repos)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        App.netComponent.inject(userViewModel)
        userViewModel.init(user)

        userViewModel.getUser()?.observe(this, Observer {
            if (it != null) {
                login.text = it.login
                name.text = it.name
                email.text = "email: ${it.email}"
                location.text = it.location
                publicRepos.text = "repos: ${it.publicRepos}"
                it.avatarUrl.let {
                    Picasso.get().load(it.toString())
                            .placeholder(R.mipmap.ic_account_circle_black_24dp)
                            .fit()
                            .into(avatar)
                }
            }
        })
    }

    companion object {
        fun newInstance(): Fragment = UserFragment()
    }
}
