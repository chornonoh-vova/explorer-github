package com.hbvhuwe.explorergithub.ui

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.ui.adapters.BaseViewPagerAdapter
import com.hbvhuwe.explorergithub.ui.fragments.FilesFragment
import com.hbvhuwe.explorergithub.ui.fragments.NoInternetFragment
import com.hbvhuwe.explorergithub.ui.fragments.RepoOverviewFragment
import com.hbvhuwe.explorergithub.viewmodel.RepositoryViewModel


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
    private val viewPager by lazy {
        findViewById<ViewPager>(R.id.repo_view_pager)
    }

    private lateinit var repositoryViewModel: RepositoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        setSupportActionBar(findViewById(R.id.toolbar_repo))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$repoOwner/$repoName"

        val adapter = ViewPagerAdapter(supportFragmentManager, this, repoOwner, repoName, arrayOf(
                R.string.tab_overview_text,
                R.string.tab_code_text,
                R.string.tab_issues_text,
                R.string.tab_pr_text,
                R.string.tab_projects_text))
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        repositoryViewModel = ViewModelProviders.of(this).get(RepositoryViewModel::class.java)
        App.netComponent?.inject(repositoryViewModel)
    }

    override fun onBackPressed() {
        val fr = supportFragmentManager.findFragmentByTag("android:switcher:${R.id.repo_view_pager}:${viewPager.currentItem}")
        if (fr is FilesFragment) {
            if (fr.isBackPressAllowed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun retry() {
        // TODO: move activity initialization here
    }

    class ViewPagerAdapter(
            fm: FragmentManager,
            context: Context,
            private val owner: String,
            private val name: String,
            titles: Array<Int>
    ): BaseViewPagerAdapter(fm, context, titles) {
        override fun getFragment(position: Int): Fragment {
            val args = Bundle()
            args.putString(Const.REPO_OWNER_KEY, owner)
            args.putString(Const.REPO_NAME_KEY, name)
            var fragment: Fragment = NoInternetFragment()
            when (position) {
                0 -> {
                    fragment = RepoOverviewFragment()
                    fragment.arguments = args
                }
                1 -> {
                    fragment = FilesFragment()
                    fragment.arguments = args
                }
            }
            return fragment
        }
    }
}
