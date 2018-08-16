package com.hbvhuwe.explorergithub.ui

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.model.GitHubBranch
import com.hbvhuwe.explorergithub.model.GitHubCommit
import com.hbvhuwe.explorergithub.model.Repo
import com.hbvhuwe.explorergithub.showToast
import com.hbvhuwe.explorergithub.ui.fragments.FilesFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


class RepoActivity : AppCompatActivity() {
    private lateinit var repo: Repo
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
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPrefs.getBoolean("dark_theme", false))
            setTheme(R.style.AppThemeDark)
        else
            setTheme(R.style.AppThemeLight)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        setSupportActionBar(findViewById(R.id.toolbar_repo))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState != null) {
            fragment = supportFragmentManager.findFragmentByTag("fragment") as FilesFragment
            fragment.repoActivity = this

            repo = savedInstanceState.getSerializable("repository") as Repo
            repositoryName.text = savedInstanceState.getString("repo_name")
            repositoryStars.text = savedInstanceState.getString("repo_stars")
            repositoryCommits.text = savedInstanceState.getString("repo_commits")
            repositoryBranches.text = savedInstanceState.getString("repo_branches")
            findViewById<ProgressBar>(R.id.loading_panel_activity_repo).visibility = View.GONE
        } else {
            repo = intent.getSerializableExtra("repository") as Repo

            repositoryName.text = repo.fullName
            repositoryStars.text = repo.starsCount.toString()
            countCommits()
            countBranches()

            fragment = FilesFragment.newInstance()
            val bundle = Bundle()
            bundle.putString("currentPath", repo.contentsURL.toString().removeSuffix("{+path}"))
            bundle.putSerializable("repoObject", repo)
            fragment.arguments = bundle
            fragment.repoActivity = this

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.files_frame_layout, fragment, "fragment")
                commit()
            }
            findViewById<ProgressBar>(R.id.loading_panel_activity_repo).visibility = View.GONE
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
        val call = App.api.getBranchesForRepo(repo.owner.login, repo.name)
        call.enqueue(branchesCallback)
    }

    private fun countCommits() {
        val call = App.api.getCommitsOfRepo(repo.owner.login, repo.name)
        call.enqueue(commitsCallback)
    }

    fun updateFiles(url: URL) {
        fragment = FilesFragment.newInstance()
        val bundle = Bundle()
        bundle.putString("currentPath", url.toString())
        bundle.putSerializable("repoObject", repo)
        fragment.arguments = bundle
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
