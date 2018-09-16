package com.hbvhuwe.explorergithub.ui

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.ui.adapters.BaseViewPagerAdapter
import com.hbvhuwe.explorergithub.ui.fragments.NoInternetFragment


class RepoActivity : BaseActivity(), NoInternetFragment.IRetryActivity {

    private val repoName by lazy {
        intent.getStringExtra(Const.REPO_NAME_KEY)
    }
    private val repoOwner by lazy {
        intent.getStringExtra(Const.REPO_OWNER_KEY)
    }
    private val tabLayout by lazy {
        findViewById<TabLayout>(R.id.tab_layout_repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        setSupportActionBar(findViewById(R.id.toolbar_repo))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$repoOwner/$repoName"

        val viewPager = findViewById<ViewPager>(R.id.repo_view_pager)

        val adapter = ViewPagerAdapter(supportFragmentManager, this, arrayOf(
                R.string.tab_overview_text,
                R.string.tab_code_text,
                R.string.tab_issues_text,
                R.string.tab_pr_text,
                R.string.tab_projects_text))
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun retry() {
        // TODO: move activity initialization here
    }

    class ViewPagerAdapter(
            fm: FragmentManager,
            context: Context,
            titles: Array<Int>
    ): BaseViewPagerAdapter(fm, context, titles) {
        override fun getFragment(position: Int): Fragment {
            return NoInternetFragment()
        }
    }
}
