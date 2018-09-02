package com.hbvhuwe.explorergithub.ui

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R


class RepoActivity : BaseActivity() {
    private val repoName by lazy {
        intent.getStringExtra(Const.REPO_NAME_KEY)
    }
    private val repoOwner by lazy {
        intent.getStringExtra(Const.REPO_OWNER_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        setSupportActionBar(findViewById(R.id.toolbar_repo))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
