package com.hbvhuwe.explorergithub

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.hbvhuwe.explorergithub.fragments.FilesFragment
import com.hbvhuwe.explorergithub.models.GitHubBranch
import com.hbvhuwe.explorergithub.models.GitHubCommit
import com.hbvhuwe.explorergithub.models.GitHubRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


class RepoActivity : AppCompatActivity() {
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
    private lateinit var fragment: FilesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        setSupportActionBar(findViewById(R.id.toolbar_repo))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState != null) {
            fragment = supportFragmentManager.findFragmentByTag("fragment") as FilesFragment
            fragment.repoActivity = this

            repo = savedInstanceState.getSerializable("repository") as GitHubRepo
            repositoryName.text = savedInstanceState.getString("repo_name")
            repositoryStars.text = savedInstanceState.getString("repo_stars")
            repositoryCommits.text = savedInstanceState.getString("repo_commits")
            repositoryBranches.text = savedInstanceState.getString("repo_branches")
        } else {
            repo = intent.getSerializableExtra("repository") as GitHubRepo

            repositoryName.text = repo.fullName
            repositoryStars.text = repo.starsCount.toString()
            countCommits()
            countBranches()

            fragment = FilesFragment.newInstance()
            fragment.currentPath = repo.contentsURL.toString().removeSuffix("{+path}")
            fragment.repoActivity = this

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

    private fun countBranches() {
        val call = App.client.getBranchesForRepo("hbvhuwe", repo.name)
        call.enqueue(branchesCallback)
    }

    private fun countCommits() {
        val call = App.client.getCommitsOfRepo("hbvhuwe", repo.name)
        call.enqueue(commitsCallback)
    }

    fun updateFiles(url: URL) {
        fragment = FilesFragment.newInstance()
        fragment.currentPath = url.toString()
        fragment.repoActivity = this

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.files_frame_layout, fragment, "fragment")
            addToBackStack("fragment")
            commit()
        }
    }

    private val branchesCallback = object : Callback<List<GitHubBranch>> {
        override fun onFailure(call: Call<List<GitHubBranch>>?, t: Throwable?) {
            showToast("Network error: " + t?.message)
        }

        override fun onResponse(call: Call<List<GitHubBranch>>?, response: Response<List<GitHubBranch>>?) {
            if (response != null) {
                if (response.isSuccessful) {
                    val branches = response.body()!!
                    repositoryBranches.text = branches.size.toString()
                }
            }
        }

    }

    private val commitsCallback = object : Callback<List<GitHubCommit>> {
        override fun onFailure(call: Call<List<GitHubCommit>>?, t: Throwable?) {
            showToast("Network error: " + t?.message)
        }

        override fun onResponse(call: Call<List<GitHubCommit>>?, response: Response<List<GitHubCommit>>?) {
            if (response != null) {
                if (response.isSuccessful) {
                    val commits = response.body()!!
                    repositoryCommits.text = commits.size.toString()
                }
            }
        }

    }
}
