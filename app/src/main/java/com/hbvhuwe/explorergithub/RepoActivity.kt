package com.hbvhuwe.explorergithub

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.hbvhuwe.explorergithub.fragments.FilesFragment

class RepoActivity : AppCompatActivity() {
    private lateinit var repositoryName: TextView
    private lateinit var repositoryStars: TextView
    private lateinit var repositoryCommits: TextView
    private lateinit var repositoryBranches: TextView
    private lateinit var fragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        repositoryName = findViewById(R.id.repository_name)
        repositoryStars = findViewById(R.id.repository_stars)
        repositoryCommits = findViewById(R.id.repository_commits)
        repositoryBranches = findViewById(R.id.repository_branches)

        if (savedInstanceState != null) {
            fragment = supportFragmentManager.findFragmentByTag("fragment")

            repositoryName.text = savedInstanceState.getString("repo_name")
            repositoryStars.text = savedInstanceState.getString("repo_stars")
            repositoryCommits.text = savedInstanceState.getString("repo_commits")
            repositoryBranches.text = savedInstanceState.getString("repo_branches")
        } else {
            val fullName = intent.getStringExtra("fullName")
            fullPath = "$fullName/contents"

            repositoryName.text = fullName
            repositoryStars.text = "0"
            repositoryCommits.text = countCommits()
            repositoryBranches.text = countBranches()

            fragment = FilesFragment.newInstance()

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.files_frame_layout, fragment, "fragment")
                commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString("repo_name", repositoryName.text.toString())
        outState?.putString("repo_stars", repositoryStars.text.toString())
        outState?.putString("repo_commits", repositoryCommits.text.toString())
        outState?.putString("repo_branches", repositoryBranches.text.toString())
    }

    private fun countBranches(): String {
        return "0"
    }

    private fun countCommits(): String {
        return "0"
    }

    companion object {
        lateinit var fullPath: String
    }
}
