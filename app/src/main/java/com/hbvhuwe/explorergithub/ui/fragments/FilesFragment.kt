package com.hbvhuwe.explorergithub.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.model.File
import com.hbvhuwe.explorergithub.ui.FileActivity
import com.hbvhuwe.explorergithub.ui.RecyclerViewTouchListener
import com.hbvhuwe.explorergithub.ui.adapters.FilesAdapter
import com.hbvhuwe.explorergithub.viewmodel.FilesViewModel

class FilesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var filesLoading: LinearLayout
    private lateinit var currentPath: TextView
    private lateinit var filesViewModel: FilesViewModel
    private lateinit var filesAdapter: FilesAdapter
    private lateinit var branchSelector: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files, container, false)
    }

    private lateinit var user: String
    private lateinit var repo: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.files_list)
        filesLoading = view.findViewById(R.id.files_loading)
        currentPath = view.findViewById(R.id.current_location)
        branchSelector = view.findViewById(R.id.branch_selector_spinner)

        user = arguments!!.getString(Const.REPO_OWNER_KEY)!!
        repo = arguments!!.getString(Const.REPO_NAME_KEY)!!

        filesAdapter = FilesAdapter(emptyList())

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
            adapter = filesAdapter
            addOnItemTouchListener(RecyclerViewTouchListener(
                    activity!!.applicationContext,
                    this,
                    object : RecyclerViewTouchListener.ClickListener {
                        override fun onClick(view: View, vh: RecyclerView.ViewHolder) {
                            vh as FilesAdapter.ViewHolder
                            if (vh.file.isDirectory()) {
                                toggleLoading()
                                filesViewModel.setCurrentPath(vh.file.path)
                                filesViewModel.getFiles().observe(this@FilesFragment, filesObserver)
                            } else if (vh.file.isPicture()) {
                                Toast.makeText(activity, "This is picture", Toast.LENGTH_SHORT).show()
                            } else if (vh.file.isBinary()) {
                                Toast.makeText(activity, "This is binary file", Toast.LENGTH_SHORT).show()
                            } else if (vh.file.isRegularFile()) {
                                val intent = Intent(activity, FileActivity::class.java).apply {
                                    putExtra(Const.FILE_URL_KEY, vh.file.downloadUrl.toString())
                                }
                                activity?.startActivity(intent)
                            }
                        }

                        override fun onLongClick(view: View, vh: RecyclerView.ViewHolder) {

                        }

                    }
            ))
        }

        filesViewModel = ViewModelProviders.of(this).get(FilesViewModel::class.java)
        App.netComponent?.inject(filesViewModel)
        filesViewModel.init(user, repo)

        filesViewModel.currentPath.observe(viewLifecycleOwner, Observer {
            currentPath.text = "/$it"
        })

        filesViewModel.getBranches().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val branches = Array(it.size) { i ->
                    it[i].name
                }

                val adapter = ArrayAdapter<String>(
                        activity!!.applicationContext,
                        R.layout.spinner_item,
                        branches
                )
                branchSelector.apply {
                    this.adapter = adapter
                    setSelection(adapter.getPosition("master"))
                    onItemSelectedListener = spinnerSelectedListener
                }
            }
        })

        filesViewModel.getFiles().observe(this, filesObserver)
    }

    private val filesObserver = Observer<List<File>> {
        if (it != null) {
            toggleLoading()
            filesAdapter.dataset = it
        }
    }

    private val spinnerSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val selected = parent.getItemAtPosition(position).toString()
            filesViewModel.branch = selected
            filesViewModel.currentPath.value = ""
            toggleLoading()
            filesViewModel.getFiles().observe(this@FilesFragment, filesObserver)
        }

    }

    private fun toggleLoading() {
        if (filesLoading.visibility == View.VISIBLE) {
            filesLoading.visibility = View.GONE
        } else {
            filesLoading.visibility = View.VISIBLE
        }
    }

    fun isBackPressAllowed(): Boolean {
        if (currentPath.text == "/") return true
        val beginPath = currentPath.text.toString()
        val lastFolderIndex = beginPath.lastIndexOf('/')
        val path = beginPath.removeRange(lastFolderIndex, beginPath.length).removePrefix("/")
        filesLoading.visibility = View.VISIBLE
        filesViewModel.setCurrentPath(path)
        filesViewModel.getFiles().observe(this, filesObserver)
        return false
    }

    companion object {
        fun newInstance(): FilesFragment = FilesFragment()
    }
}
