package com.hbvhuwe.explorergithub.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.widget.Toast
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.net.Credentials
import com.hbvhuwe.explorergithub.ui.SplashActivity

class SettingsFragment: PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        val logoutButton = findPreference("logout")

        logoutButton.setOnPreferenceClickListener {
            (activity.application as App).saveCredentials(Credentials.empty())
            val intent = Intent(activity, SplashActivity::class.java)
            startActivity(intent)
            activity.finish()
            true
        }

        val themePreference = findPreference("dark_theme")

        themePreference.setOnPreferenceClickListener {
            Toast.makeText(activity, "Restart application to take effect", Toast.LENGTH_SHORT).show()
            true
        }
    }
}