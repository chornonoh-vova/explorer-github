package com.hbvhuwe.explorergithub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.viewmodel.RepositoryViewModel

class RepoOverviewFragment: Fragment() {
    private lateinit var topics: ChipGroup
    private lateinit var readmeView: WebView
    private lateinit var repositoryViewModel: RepositoryViewModel

    private lateinit var user: String
    private lateinit var repo: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_overview, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = arguments!!.getString(Const.REPO_OWNER_KEY)!!
        repo = arguments!!.getString(Const.REPO_NAME_KEY)!!

        readmeView = view.findViewById(R.id.readme_view)
        topics = view.findViewById(R.id.repo_topics)

        repositoryViewModel = ViewModelProviders.of(this).get(RepositoryViewModel::class.java)
        App.netComponent?.inject(repositoryViewModel)

        repositoryViewModel.init(user, repo)

        repositoryViewModel.getTopics(user, repo)?.observe(viewLifecycleOwner, Observer { topic ->
            topic.names.forEach {
                val chip = Chip(activity)
                chip.text = it
                chip.isClickable = false
                chip.isCheckable = false
                topics.addView(chip)
            }
        })

        repositoryViewModel.getReadme(user, repo)?.observe(viewLifecycleOwner, Observer { file ->
            repositoryViewModel.getFile(file)?.observe(viewLifecycleOwner, Observer { markdown ->
                repositoryViewModel.getReadmeHtml(markdown, "$user/$repo")?.observe(viewLifecycleOwner, Observer { html ->
                    val data = """
                    <html>
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1">
                        <link href="markdown-style.css" type="text/css" rel="stylesheet"/>
                    </head>
                    <body class="markdown-body">$html</body>
                    </html>""".trimMargin()
                    readmeView.loadDataWithBaseURL("file:///android_asset/", data,  "text/html", "UTF-8", null)
                })
            })
        })
    }
}