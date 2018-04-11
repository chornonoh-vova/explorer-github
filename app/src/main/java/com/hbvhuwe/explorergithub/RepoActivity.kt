package com.hbvhuwe.explorergithub

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.gson.Gson
import com.hbvhuwe.explorergithub.fragments.FilesFragment
import com.hbvhuwe.explorergithub.models.GitHubBranch
import com.hbvhuwe.explorergithub.models.GitHubCommit
import com.hbvhuwe.explorergithub.models.GitHubRepo
import com.hbvhuwe.explorergithub.network.DownloadInfo
import com.hbvhuwe.explorergithub.network.LoadInfo


class RepoActivity : AppCompatActivity(), LoadInfo {
    private lateinit var repo: GitHubRepo
    private val repositoryName by lazy {
        findViewById<TextView>(R.id.repository_name)
    }
    private val repositoryStars by lazy {
        findViewById<TextView>(R.id.repository_stars)
    }
    private val repositoryCommits by lazy {
        findViewById<TextView>(R.id.repository_commits)
    }
    private val repositoryBranches by lazy {
        findViewById<TextView>(R.id.repository_branches)
    }
    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        setSupportActionBar(findViewById(R.id.toolbar_repo))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState != null) {
            fragment = supportFragmentManager.findFragmentByTag("fragment")

            repo = savedInstanceState.getSerializable("repository") as GitHubRepo
            repositoryName.text = savedInstanceState.getString("repo_name")
            repositoryStars.text = savedInstanceState.getString("repo_stars")
            repositoryCommits.text = savedInstanceState.getString("repo_commits")
            repositoryBranches.text = savedInstanceState.getString("repo_branches")
        } else {
            repo = intent.getSerializableExtra("repository") as GitHubRepo
            fullPath = "${repo.fullName}/contents"

            repositoryName.text = repo.fullName
            repositoryStars.text = repo.starsCount.toString()
            countCommits()
            countBranches()

            fragment = FilesFragment.newInstance()

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.files_frame_layout, fragment, "fragment")
                commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putSerializable("repository", repo)
        outState?.putString("repo_name", repositoryName.text.toString())
        outState?.putString("repo_stars", repositoryStars.text.toString())
        outState?.putString("repo_commits", repositoryCommits.text.toString())
        outState?.putString("repo_branches", repositoryBranches.text.toString())
    }

    override fun onLoadInfoCallback(tag: LoadInfo.Tags, result: String?) {
        if (tag == LoadInfo.Tags.COMMITS) {
            //commits loaded
            val commits = Gson().fromJson(result, Array<GitHubCommit>::class.java)
            repositoryCommits.text = commits.size.toString()
        } else if (tag == LoadInfo.Tags.BRANCHES) {
            //branches loaded
            val branches = Gson().fromJson(result, Array<GitHubBranch>::class.java)
            repositoryBranches.text = branches.size.toString()
        }
    }

    override fun onErrorCallback(tag: LoadInfo.Tags) {
        if (tag == LoadInfo.Tags.COMMITS) {
            showToast("Network error while loading commits")
        } else if (tag == LoadInfo.Tags.BRANCHES) {
            showToast("Network error while loading branches")
        }
    }

    private fun countBranches() {
        DownloadInfo(this, LoadInfo.Tags.BRANCHES).execute(
                "https://api.github.com/repos/${repo.fullName}/branches"
        )
    }

    private fun countCommits() {
        DownloadInfo(this, LoadInfo.Tags.COMMITS).execute(
                "https://api.github.com/repos/${repo.fullName}/commits"
        )
    }

    companion object {
        lateinit var fullPath: String
    }
}
