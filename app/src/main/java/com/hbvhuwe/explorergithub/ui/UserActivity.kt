package com.hbvhuwe.explorergithub.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.isOnline
import com.hbvhuwe.explorergithub.ui.adapters.BaseViewPagerAdapter
import com.hbvhuwe.explorergithub.ui.fragments.NoInternetFragment
import com.hbvhuwe.explorergithub.ui.fragments.ReposFragment
import com.hbvhuwe.explorergithub.ui.fragments.UserFragment
import com.hbvhuwe.explorergithub.ui.fragments.UsersFragment


class UserActivity : BaseActivity(), NoInternetFragment.IRetryActivity {
    override fun retry() {
        update()
    }

    private var offlineMode = false
    private val tabCount = 5
    private val user by lazy {
        getUserLogin()
    }
    private val tabLayout by lazy {
        findViewById<TabLayout>(R.id.tab_layout_main)
    }

    private val coordinatorLayout by lazy {
        findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(findViewById(R.id.toolbar_main))

        update()
    }

    private fun update() {
        App.netComponent = (application as App).createNetComponent()

        val viewPager = findViewById<ViewPager>(R.id.main_view_pager)
        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().remove(it).commitNow()
        }

        this.offlineMode = !isOnline()

        if (!isOnline()) {
            Snackbar.make(coordinatorLayout, R.string.network_error_text, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.YELLOW)
                    .setAction(R.string.settings_text) {
                        showWifiSettings()
                    }.show()
        }

        val adapter = ViewPagerAdapter(supportFragmentManager, this, offlineMode, user)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = tabCount - 1

        tabLayout.setupWithViewPager(viewPager)
    }

    private fun getUserLogin() =
            intent.getStringExtra(Const.USER_KEY) ?: Const.USER_LOGGED_IN

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.user_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_update -> { update() ; true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showWifiSettings() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    class ViewPagerAdapter(
            fm: FragmentManager,
            context: Context,
            private val offlineMode: Boolean,
            private val user: String
    ): BaseViewPagerAdapter(fm, context, arrayOf(
            R.string.tab_overview_text,
            R.string.tab_repos_text,
            R.string.tab_starred_text,
            R.string.tab_followers_text,
            R.string.tab_following_text)) {
        override fun getFragment(position: Int): Fragment {
            if (offlineMode)
                return NoInternetFragment()

            val args = Bundle()
            args.putString(Const.USER_KEY, user)
            val fragment = when(position) {
                0 -> UserFragment.newInstance()
                1 -> {
                    args.putInt(Const.REPOS_MODE_KEY, Const.REPOS_MODE_REPOS)
                    ReposFragment.newInstance()
                }
                2 -> {
                    args.putInt(Const.REPOS_MODE_KEY, Const.REPOS_MODE_STARRED)
                    ReposFragment.newInstance()
                }
                3 -> {
                    args.putInt(Const.USERS_MODE_KEY, Const.USERS_MODE_FOLLOWERS)
                    UsersFragment.newInstance()
                }
                4 -> {
                    args.putInt(Const.USERS_MODE_KEY, Const.USERS_MODE_FOLLOWING)
                    UsersFragment.newInstance()
                }
                else -> androidx.fragment.app.Fragment()
            }
            fragment.arguments = args
            return fragment
        }
    }
}
