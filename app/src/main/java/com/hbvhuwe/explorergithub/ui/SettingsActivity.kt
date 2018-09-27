package com.hbvhuwe.explorergithub.ui

import android.os.Bundle
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.ui.fragments.SettingsFragment


class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
                .replace(R.id.settingsFragment, SettingsFragment())
                .commit()
    }
}
