package com.hbvhuwe.explorergithub.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder

import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.isOnline
import com.hbvhuwe.explorergithub.models.GitHubRepo
import com.hbvhuwe.explorergithub.network.DownloadInfo
import com.hbvhuwe.explorergithub.network.LoadInfo
import com.hbvhuwe.explorergithub.showToast

class ReposFragment : Fragment(), LoadInfo {
    private lateinit var recyclerView: RecyclerView
    private lateinit var repositoriesAdapter: RepositoriesAdapter
    private lateinit var repos: Array<GitHubRepo>
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_repos, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view!!.findViewById(R.id.repositories_view)
        if (isOnline()) {
            DownloadInfo(this).execute("https://api.github.com/users/hbvhuwe/repos")
        } else {
            showToast("Internet not available")
        }
    }

    companion object {
        fun newInstance() = ReposFragment()
    }


    override fun onLoadInfoCallback(result: String?) {
        repos = GsonBuilder().create().fromJson(result, Array<GitHubRepo>::class.java)
        setupRecycler()
    }

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        repositoriesAdapter = RepositoriesAdapter(repos)
        recyclerView.adapter = repositoriesAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
    }
}
