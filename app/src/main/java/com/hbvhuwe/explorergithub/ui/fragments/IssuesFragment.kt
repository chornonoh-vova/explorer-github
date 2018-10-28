package com.hbvhuwe.explorergithub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
import com.hbvhuwe.explorergithub.ui.adapters.IssuesAdapter
import com.hbvhuwe.explorergithub.viewmodel.IssuesViewModel

class IssuesFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var listLoading: LinearLayout
    private lateinit var listLoadingText: TextView
    private lateinit var issuesAdapter: IssuesAdapter
    private lateinit var noContentText: TextView
    private lateinit var issuesViewModel: IssuesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_list, container, false)

    private lateinit var user: String
    private lateinit var repo: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.content_list)
        listLoading = view.findViewById(R.id.list_loading)
        listLoadingText = view.findViewById(R.id.list_loading_text)
        noContentText = view.findViewById(R.id.no_content_text)

        user = arguments!!.getString(Const.REPO_OWNER_KEY)!!
        repo = arguments!!.getString(Const.REPO_NAME_KEY)!!

        issuesAdapter = IssuesAdapter(emptyList())

        listLoadingText.text = getString(R.string.issues_loading_text)
        noContentText.text = getString(R.string.no_issues_text)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
            adapter = issuesAdapter
        }

        issuesViewModel = ViewModelProviders.of(this).get(IssuesViewModel::class.java)
        App.netComponent?.inject(issuesViewModel)
        issuesViewModel.init(user, repo)

        issuesViewModel.getIssues().observe(this, Observer {
            if (it != null) {
                listLoading.visibility = View.GONE
                issuesAdapter.dataset = it
                if (it.isEmpty()) {
                    noContentText.visibility = View.VISIBLE
                }
            }
        })
    }
}