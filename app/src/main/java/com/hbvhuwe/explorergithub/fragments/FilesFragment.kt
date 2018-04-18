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
import com.hbvhuwe.explorergithub.adapters.FilesAdapter
import com.hbvhuwe.explorergithub.isOnline
import com.hbvhuwe.explorergithub.models.GitHubFile
import com.hbvhuwe.explorergithub.network.DownloadFile
import com.hbvhuwe.explorergithub.network.LoadInfo
import com.hbvhuwe.explorergithub.showToast

class FilesFragment : Fragment(), LoadInfo {
    private lateinit var recyclerView: RecyclerView
    private lateinit var filesAdapter: FilesAdapter
    private lateinit var files: Array<GitHubFile>
    lateinit var currentPath: String
    lateinit var repoActivity: RepoActivity
    private val fullFilePath by lazy {
        view!!.findViewById<TextView>(R.id.full_file_path)
    }

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
            fullFilePath.text = savedInstanceState.getCharSequence("pathToDisplay")
            setupRecycler()
        } else {
            if (isOnline()) {
                DownloadFile(this).execute(this.currentPath)
            } else {
                showToast("Internet not available")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("files", files)
        outState.putCharSequence("pathToDisplay", fullFilePath.text)
    }

    companion object {
        fun newInstance(): FilesFragment = FilesFragment()
    }

    private fun setupRecycler() {
        recyclerView.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        filesAdapter = FilesAdapter(files)
        filesAdapter.filesFragment = this
        recyclerView.adapter = filesAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        var pathToDisplay = currentPath.removePrefix("https://api.github.com/repos/")
        if (pathToDisplay.endsWith("?ref=master")) {
            pathToDisplay = pathToDisplay.removeSuffix("?ref=master")
            pathToDisplay = pathToDisplay.replace("contents/", "")
        } else {
            pathToDisplay = pathToDisplay.replace("contents/", "")
        }
        fullFilePath.text = pathToDisplay
    }

    override fun onLoadInfoCallback(result: String?) {
            files = GsonBuilder().create().fromJson(result, Array<GitHubFile>::class.java)
            setupRecycler()
    }

    override fun onErrorCallback() {
            showToast("Network error while loading files info")
    }
}
