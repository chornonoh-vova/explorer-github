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
    private lateinit var reposAdapter: ReposAdapter
    private lateinit var repos: Array<GitHubRepo>
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_repos, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view!!.findViewById(R.id.repositories_view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            repos = savedInstanceState.getSerializable("repos") as Array<GitHubRepo>
            setupRecycler()
        } else {
            if (isOnline()) {
                DownloadInfo(this, LoadInfo.Tags.REPOS).execute("https://api.github.com/users/hbvhuwe/repos")
            } else {
                showToast("Internet not available")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable("repos", repos)
    }

    companion object {
        fun newInstance() = ReposFragment()
    }


    override fun onLoadInfoCallback(tag: LoadInfo.Tags, result: String?) {
        if (tag == LoadInfo.Tags.REPOS) {
            repos = GsonBuilder().create().fromJson(result, Array<GitHubRepo>::class.java)
            setupRecycler()
        }
    }

    override fun onErrorCallback(tag: LoadInfo.Tags) {
        if (tag == LoadInfo.Tags.REPOS) {
            showToast("Network error while loading repos info")
        }
    }

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        reposAdapter = ReposAdapter(repos)
        recyclerView.adapter = reposAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
    }
}
