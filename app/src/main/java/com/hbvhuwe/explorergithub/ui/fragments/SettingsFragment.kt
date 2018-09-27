package com.hbvhuwe.explorergithub.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.net.Credentials
import com.hbvhuwe.explorergithub.ui.SplashActivity

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        setPreferencesFromResource(R.xml.preferences, p1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val logoutButton = findPreference("logout")

        logoutButton.setOnPreferenceClickListener {
            (activity?.application as App).saveCredentials(Credentials.empty())
            val intent = Intent(activity, SplashActivity::class.java)
            startActivity(intent)
            activity!!.finish()
            true
        }
    }
}