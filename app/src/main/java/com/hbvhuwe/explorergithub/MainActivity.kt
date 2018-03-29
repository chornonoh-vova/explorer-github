package com.hbvhuwe.explorergithub

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.hbvhuwe.explorergithub.fragments.ReposFragment
import com.hbvhuwe.explorergithub.fragments.SearchFragment
import com.hbvhuwe.explorergithub.fragments.UserFragment


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.navigation)


        bottomNavigationView.setOnNavigationItemSelectedListener{
            var selectedFragment : Fragment? = null
            when (it.itemId) {
                R.id.action_user -> selectedFragment = UserFragment.newInstance()
                R.id.action_repos -> selectedFragment = ReposFragment.newInstance()
                R.id.action_search -> selectedFragment = SearchFragment.newInstance()
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, selectedFragment)
            transaction.commit()
            true
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, UserFragment.newInstance())
        transaction.commit()
    }
}
