package com.hbvhuwe.explorergithub.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.GsonBuilder

import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.RepoActivity
import com.hbvhuwe.explorergithub.isOnline
import com.hbvhuwe.explorergithub.models.GitHubFile
import com.hbvhuwe.explorergithub.network.DownloadInfo
import com.hbvhuwe.explorergithub.network.LoadInfo
import com.hbvhuwe.explorergithub.showToast

class FilesFragment : Fragment(), LoadInfo {
    private lateinit var recyclerView: RecyclerView
    private lateinit var filesAdapter: FilesAdapter
    private lateinit var files: Array<GitHubFile>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view!!.findViewById(R.id.files_view)
        if (isOnline()) {
            DownloadInfo(this).execute("https://api.github.com/repos/${RepoActivity.fullPath}")
        } else {
            showToast("Internet not available")
        }
    }

    companion object {
        fun newInstance(): Fragment = FilesFragment()
    }

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        filesAdapter = FilesAdapter(files)
        recyclerView.adapter = filesAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
    }

    override fun onLoadInfoCallback(result: String?) {
        files = GsonBuilder().create().fromJson(result, Array<GitHubFile>::class.java)
        setupRecycler()
    }
}
