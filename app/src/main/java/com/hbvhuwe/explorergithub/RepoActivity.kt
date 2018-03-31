package com.hbvhuwe.explorergithub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.hbvhuwe.explorergithub.fragments.FilesFragment

class RepoActivity : AppCompatActivity() {
    private lateinit var repositoryName: TextView
    private lateinit var repositoryStars: TextView
    private lateinit var repositoryCommits: TextView
    private lateinit var repositoryBranches: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        repositoryName = findViewById(R.id.repository_name)
        repositoryStars = findViewById(R.id.repository_stars)
        repositoryCommits = findViewById(R.id.repository_commits)
        repositoryBranches = findViewById(R.id.repository_branches)

        val fullName = intent.getStringExtra("fullName")
        fullPath = "$fullName/contents"

        repositoryName.text = fullName
        repositoryStars.text = "0"
        repositoryCommits.text = countCommits()
        repositoryBranches.text = countBranches()

        val fragment = FilesFragment.newInstance()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.files_frame_layout, fragment)
        transaction.commit()
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
