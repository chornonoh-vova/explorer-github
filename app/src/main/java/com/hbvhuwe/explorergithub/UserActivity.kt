package com.hbvhuwe.explorergithub

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.hbvhuwe.explorergithub.fragments.ReposFragment
import com.hbvhuwe.explorergithub.fragments.SearchFragment
import com.hbvhuwe.explorergithub.fragments.StarredReposFragment
import com.hbvhuwe.explorergithub.fragments.UserFragment


class UserActivity : AppCompatActivity() {

    private val tabLayout by lazy {
        findViewById<TabLayout>(R.id.tab_layout_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(findViewById(R.id.toolbar_main))

//        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_user_text))
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_repos_text))
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_starred_text))
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_search_text))

        val viewPager = findViewById<ViewPager>(R.id.main_view_pager)
        val adapter = ViewPagerAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

        })
    }

    class ViewPagerAdapter(supportFragmentManager: FragmentManager?, private val tabCount: Int)
        : FragmentStatePagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int) = when(position) {
            0 -> UserFragment.newInstance()
            1 -> ReposFragment.newInstance()
            2 -> StarredReposFragment.newInstance()
            3 -> SearchFragment.newInstance()
            else -> throw Exception("No fragment")
        }

        override fun getCount() = tabCount
    }
}
