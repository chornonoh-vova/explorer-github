package com.hbvhuwe.explorergithub.ui.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.ui.adapters.UsersAdapter
import com.hbvhuwe.explorergithub.viewmodel.UserViewModel

class UsersFragment : Fragment() {
    private var mode = 0
    private lateinit var user: String
    private lateinit var repo: String
    private lateinit var listLoading: LinearLayout
    private lateinit var listLoadingText: TextView
    private lateinit var noContentText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.content_list)
        listLoading = view.findViewById(R.id.list_loading)
        listLoadingText = view.findViewById(R.id.list_loading_text)
        noContentText = view.findViewById(R.id.no_content_text)

        mode = arguments?.getInt(Const.USERS_MODE_KEY) ?: 0
        user = arguments?.getString(Const.USER_KEY) ?: Const.USER_LOGGED_IN
        repo = arguments?.getString(Const.REPO_NAME_KEY) ?: ""

        usersAdapter = UsersAdapter(emptyList())

        when (mode) {
            Const.USERS_MODE_FOLLOWERS -> {
                listLoadingText.text = activity?.getText(R.string.followers_loading_text)
                noContentText.text = activity?.getText(R.string.no_followers_text)
            }
            Const.USERS_MODE_FOLLOWING -> {
                listLoadingText.text = activity?.getText(R.string.following_loading_text)
                noContentText.text = activity?.getText(R.string.no_following_text)
            }
            Const.USERS_MODE_CONTRIBUTORS -> {
                listLoadingText.text = activity?.getText(R.string.contributors_loading_text)
                noContentText.text = activity?.getText(R.string.no_contributors_text)
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
            adapter = usersAdapter
        }

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        App.netComponent?.inject(userViewModel)
        userViewModel.multipleInit(mode, user, repo)

        userViewModel.getUsers()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                listLoading.visibility = View.GONE
                usersAdapter.dataset = it
                if (it.isEmpty()) {
                    noContentText.visibility = View.VISIBLE
                }
            }
        })
    }

    companion object {
        fun newInstance() = UsersFragment()
    }
}