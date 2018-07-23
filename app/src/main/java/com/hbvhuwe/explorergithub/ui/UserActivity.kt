package com.hbvhuwe.explorergithub.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.net.Credentials
import com.hbvhuwe.explorergithub.ui.fragments.ReposFragment
import com.hbvhuwe.explorergithub.ui.fragments.UserFragment


class UserActivity : AppCompatActivity() {
    private val user by lazy {
        intent.getStringExtra(Const.USER_KEY)
    }
    private val tabLayout by lazy {
        findViewById<TabLayout>(R.id.tab_layout_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(findViewById(R.id.toolbar_main))

        App.netComponent = (application as App).createNetComponent()

        if (App.access == null) {
            val preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            val accessToken = preferences.getString("accessToken", "")
            val tokenType = preferences.getString("tokenType", "")

            App.access = Credentials(accessToken, tokenType)
        }

        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_user_text))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_repos_text))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_starred_text))

        val viewPager = findViewById<ViewPager>(R.id.main_view_pager)
        val adapter = ViewPagerAdapter(supportFragmentManager, tabLayout.tabCount, user)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = tabLayout.tabCount

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.user_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_logout -> { logout() ; true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        val credentials = Credentials.empty()
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("logged", false)
            putString("access_token", credentials.accessToken)
            putString("toke_type", credentials.tokenType)
            apply()
        }
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }

    class ViewPagerAdapter(supportFragmentManager: FragmentManager?,
                           private val tabCount: Int,
                           private val user: String)
        : FragmentStatePagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int) = getFragment(position)

        private fun getFragment(position: Int): Fragment {
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
                else -> Fragment()
            }
            fragment.arguments = args
            return fragment
        }

        override fun getCount() = tabCount
    }
}
