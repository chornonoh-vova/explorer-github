package com.hbvhuwe.explorergithub

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.hbvhuwe.explorergithub.fragments.ReposFragment
import com.hbvhuwe.explorergithub.fragments.SearchFragment
import com.hbvhuwe.explorergithub.fragments.UserFragment


class MainActivity : AppCompatActivity() {

    private val bottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.navigation)
    }
    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener{
            setupFragment(it.itemId)
            true
        }
        if (savedInstanceState != null) {
            this.fragment = supportFragmentManager.findFragmentByTag("fragment")

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout, fragment, "fragment")
                commit()
            }
        } else {
            setupFragment(currentFragment)
        }
    }

    private fun setupFragment(id: Int) {
        when (id) {
            R.id.action_user -> {
                this.fragment = UserFragment.newInstance()
                currentFragment = R.id.action_user
            }
            R.id.action_repos -> {
                this.fragment = ReposFragment.newInstance()
                currentFragment = R.id.action_repos
            }
            R.id.action_search ->  {
                this.fragment = SearchFragment.newInstance()
                currentFragment = R.id.action_search
            }
            else -> throw Exception("No Fragment")
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment, "fragment")
            commit()
        }
    }

    companion object {
        var currentFragment = R.id.action_user
    }
}
