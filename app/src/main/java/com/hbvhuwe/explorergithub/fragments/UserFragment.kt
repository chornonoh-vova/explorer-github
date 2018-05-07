package com.hbvhuwe.explorergithub.fragments

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.isOnline
import com.hbvhuwe.explorergithub.models.GitHubUser
import com.hbvhuwe.explorergithub.showToast
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment : Fragment() {
    private lateinit var login: TextView
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var publicRepos: TextView
    private lateinit var location: TextView
    private lateinit var avatar: ImageView
    private lateinit var user: GitHubUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
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
        if (savedInstanceState != null) {
            login.text = savedInstanceState.getString("login")
            name.text = savedInstanceState.getString("name")
            email.text = savedInstanceState.getString("email")
            location.text = savedInstanceState.getString("location")
            publicRepos.text = savedInstanceState.getString("publicRepos")
            avatar.setImageBitmap(savedInstanceState.getParcelable("avatar"))
        } else {
            if (isOnline()) {
                val call = App.client.getUserInfo()
                call.enqueue(userCallback)
            } else {
                showToast("Internet not available")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("login", login.text.toString())
        outState.putString("name", name.text.toString())
        outState.putString("email", email.text.toString())
        outState.putString("location", location.text.toString())
        outState.putString("publicRepos", publicRepos.text.toString())
        outState.putParcelable("avatar", (avatar.drawable as BitmapDrawable).bitmap)
    }

    companion object {
        fun newInstance(): Fragment = UserFragment()
    }

    private val userCallback = object: Callback<GitHubUser> {
        override fun onFailure(call: Call<GitHubUser>?, t: Throwable?) {
            Log.d("Error", t!!.message)
            showToast("Network error while loading user info " + t.toString())
        }

        override fun onResponse(call: Call<GitHubUser>?, response: Response<GitHubUser>?) {
            if (response != null) {
                if (response.isSuccessful) {
                    user = response.body()!!
                    Picasso.get().load(user.avatarUrl.toString()).into(avatar)
                    login.text = user.login
                    name.text = user.name
                    email.text = getString(R.string.user_email, user.email)
                    location.text = user.location
                    publicRepos.text = getString(R.string.user_repos, user.publicRepos)
                } else {
                    Log.d("Error", response.errorBody().toString())
                    showToast("Error " + response.errorBody().toString())
                }
            }
        }
    }
}
