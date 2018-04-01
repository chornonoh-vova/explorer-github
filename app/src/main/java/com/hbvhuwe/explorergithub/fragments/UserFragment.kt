package com.hbvhuwe.explorergithub.fragments

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.GsonBuilder

import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.isOnline
import com.hbvhuwe.explorergithub.models.GitHubUser
import com.hbvhuwe.explorergithub.network.DownloadImage
import com.hbvhuwe.explorergithub.network.DownloadInfo
import com.hbvhuwe.explorergithub.network.LoadInfo
import com.hbvhuwe.explorergithub.showToast

class UserFragment : Fragment(), LoadInfo {
    private lateinit var login: TextView
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var publicRepos: TextView
    private lateinit var location: TextView
    private lateinit var avatar: ImageView
    private lateinit var user: GitHubUser

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avatar = view!!.findViewById(R.id.user_avatar)
        login = view.findViewById(R.id.user_login)
        name = view.findViewById(R.id.user_name)
        email = view.findViewById(R.id.user_email)
        location = view.findViewById(R.id.user_location)
        publicRepos = view.findViewById(R.id.user_public_repos)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            login.text = savedInstanceState.getString("login")
            name.text = savedInstanceState.getString("name")
            email.text = savedInstanceState.getString("email")
            location.text = savedInstanceState.getString("location")
            publicRepos.text = savedInstanceState.getString("publicRepos")
            avatar.setImageBitmap(savedInstanceState.getParcelable("avatar"))
        } else {
            if (isOnline()) {
                DownloadInfo(this).execute("https://api.github.com/users/hbvhuwe")
            } else {
                showToast("Internet not available")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString("login", login.text.toString())
        outState?.putString("name", name.text.toString())
        outState?.putString("email", email.text.toString())
        outState?.putString("location", location.text.toString())
        outState?.putString("publicRepos", publicRepos.text.toString())
        outState?.putParcelable("avatar", (avatar.drawable as BitmapDrawable).bitmap)
    }

    companion object {
        fun newInstance() = UserFragment()
    }

    override fun onLoadInfoCallback(result: String?) {
        user = GsonBuilder().create().fromJson(result, GitHubUser::class.java)
        DownloadImage(avatar).execute(user.avatarUrl.toString())
        login.text = user.login
        name.text = user.name
        email.text = "email: ${user.email}"
        location.text = user.location
        publicRepos.text = "repos: ${user.publicRepos}"
    }

    override fun onErrorCallback(result: String?) {
        if (result != null) {
            showToast("Network error: $result")
        } else {
            showToast("Network error")
        }
    }
}
