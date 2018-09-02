package com.hbvhuwe.explorergithub.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.CircleTransform
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
    private lateinit var followButton: Button

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
        followButton = view.findViewById(R.id.follow_button)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        App.netComponent.inject(userViewModel)
        userViewModel.singleInit(user)

        userViewModel.getUser()?.observe(this, Observer { user ->
            if (user != null) {
                login.text = user.login
                name.text = user.name
                if (user.email != null) {
                    val emailAddress = user.email!!
                    email.text = getString(R.string.user_email_text)
                    email.setOnClickListener {
                        composeEmail(emailAddress)
                    }
                } else {
                    email.text = getString(R.string.user_email_text_empty)
                }
                if (user.location != null) {
                    val loc = user.location!!
                    location.text = user.location
                    location.setOnClickListener {
                        showMap(loc)
                    }
                } else {
                    location.text = getString(R.string.user_no_location_text)
                }
                publicRepos.text = getString(R.string.user_repos, user.publicRepos)
                user.avatarUrl.let {
                    Picasso.get().load(it.toString())
                            .placeholder(R.drawable.ic_account_circle)
                            .error(R.drawable.ic_error)
                            .transform(CircleTransform())
                            .fit()
                            .into(avatar)
                }
            }
        })

        if (user == Const.USER_LOGGED_IN) {
            followButton.visibility = View.GONE
        }
    }

    private fun composeEmail(vararg addresses: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        if (intent.resolveActivity(activity?.packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun showMap(location: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("geo:0,0?q=$location")
        if (intent.resolveActivity(activity?.packageManager) != null) {
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(): Fragment = UserFragment()
    }
}
