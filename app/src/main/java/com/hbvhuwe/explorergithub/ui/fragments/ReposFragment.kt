package com.hbvhuwe.explorergithub.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.ui.adapters.ReposAdapter
import com.hbvhuwe.explorergithub.viewmodel.ReposViewModel

class ReposFragment : Fragment() {
    private var mode: Int = 0
    private lateinit var user: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var reposAdapter: ReposAdapter
    private lateinit var reposViewModel: ReposViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mode = arguments!!.getInt(Const.REPOS_MODE_KEY)
        user = arguments!!.getString(Const.USER_KEY)

        reposAdapter = ReposAdapter(emptyList())

        recyclerView = RecyclerView(activity).apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
            adapter = reposAdapter
        }

        reposViewModel = ViewModelProviders.of(this).get(ReposViewModel::class.java)
        App.netComponent.inject(reposViewModel)
        reposViewModel.init(mode, user)

        reposViewModel.getRepos()?.observe(this, Observer {
            if (it != null) {
                reposAdapter.setRepos(it)
            }
        })

        return recyclerView
    }

    companion object {
        fun newInstance() = ReposFragment()
    }
}
