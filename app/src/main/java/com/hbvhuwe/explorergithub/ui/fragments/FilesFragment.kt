package com.hbvhuwe.explorergithub.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.isOnline
import com.hbvhuwe.explorergithub.model.File
import com.hbvhuwe.explorergithub.model.Repo
import com.hbvhuwe.explorergithub.showToast
import com.hbvhuwe.explorergithub.ui.RepoActivity
import com.hbvhuwe.explorergithub.ui.adapters.FilesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FilesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var filesAdapter: FilesAdapter
    private lateinit var files: ArrayList<File>
    private lateinit var currentPath: String
    lateinit var repoActivity: RepoActivity
    private lateinit var repo: Repo

    private val fullFilePath by lazy {
        view!!.findViewById<TextView>(R.id.full_file_path)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val args = arguments
        if (args != null) {
            currentPath = args.getString("currentPath")
            repo = args.getSerializable("repoObject") as Repo
        }
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
            files = savedInstanceState.getSerializable("files") as ArrayList<File>
            currentPath = savedInstanceState.getString("currentPath")
            fullFilePath.text = savedInstanceState.getString("pathToDisplay")
            setupRecycler()
        } else {
            if (isOnline()) {
                var path = currentPath.removePrefix("https://api.github.com/repos/${repo.fullName}/")
                if (path.endsWith("?ref=master")) {
                    path = path.removeSuffix("?ref=master")
                    path = path.replace("contents/", "")
                } else {
                    path = path.replace("contents/", "")
                }
                val call = App.api.getContentOfPath(repo.owner.login, repo.name, path)
                call.enqueue(filesCallback)
                val pathToDisplay = "${repo.fullName}/$path"
                fullFilePath.text = pathToDisplay
            } else {
                showToast("Internet not available")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("files", files)
        outState.putString("currentPath", currentPath)
        outState.putSerializable("pathToDisplay", fullFilePath.text.toString())
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
    }

    private val filesCallback = object : Callback<List<File>> {
        override fun onFailure(call: Call<List<File>>?, t: Throwable?) {
            showToast("Network error while loading files info")
        }

        override fun onResponse(call: Call<List<File>>?, response: Response<List<File>>?) {
            if (response != null) {
                if (response.isSuccessful) {
                    val filesResponse = response.body()!!.toMutableList()
                    filesResponse.sortWith(Comparator { o1, o2 ->
                        o1.type.compareTo(o2.type)
                    })
                    files = ArrayList()
                    files.addAll(filesResponse)
                    setupRecycler()
                }
            }
        }

    }
}
