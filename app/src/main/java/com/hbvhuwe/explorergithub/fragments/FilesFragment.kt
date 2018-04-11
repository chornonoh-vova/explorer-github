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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.files_view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            @Suppress("UNCHECKED_CAST")
            files = savedInstanceState.getSerializable("files") as Array<GitHubFile>
            setupRecycler()
        } else {
            if (isOnline()) {
                DownloadInfo(this, LoadInfo.Tags.FILES).execute("https://api.github.com/repos/${RepoActivity.fullPath}")
            } else {
                showToast("Internet not available")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("files", files)
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

    override fun onLoadInfoCallback(tag: LoadInfo.Tags, result: String?) {
        if (tag == LoadInfo.Tags.FILES) {
            files = GsonBuilder().create().fromJson(result, Array<GitHubFile>::class.java)
            setupRecycler()
        }
    }

    override fun onErrorCallback(tag: LoadInfo.Tags) {
        if (tag == LoadInfo.Tags.FILES) {
            showToast("Network error while loading files info")
        }
    }
}
