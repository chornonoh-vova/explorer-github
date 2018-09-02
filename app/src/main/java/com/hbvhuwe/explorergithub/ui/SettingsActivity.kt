package com.hbvhuwe.explorergithub.ui

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.net.Credentials


class SettingsActivity : PreferenceActivity() {

    private var mDelegate: AppCompatDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPrefs.getBoolean("dark_theme", false))
            setTheme(R.style.AppThemeDark)
        else
            setTheme(R.style.AppThemeLight)

        getDelegate().installViewFactory()
        getDelegate().onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.toolbar))
        addPreferencesFromResource(R.xml.preferences)

        val logoutButton = findPreference("logout")

        logoutButton.setOnPreferenceClickListener {
            (application as App).saveCredentials(Credentials.empty())
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
            true
        }

        val themePreference = findPreference("dark_theme")

        themePreference.setOnPreferenceClickListener {
            Toast.makeText(this, "Restart application to take effect", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        getDelegate().onPostCreate(savedInstanceState)
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        getDelegate().setContentView(layoutResID)
    }

    override fun onPostResume() {
        super.onPostResume()
        getDelegate().onPostResume()
    }

    override fun onStop() {
        super.onStop()
        getDelegate().onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        getDelegate().onDestroy()
    }

    private fun setSupportActionBar(@Nullable toolbar: Toolbar) {
        getDelegate().setSupportActionBar(toolbar)
    }

    private fun getDelegate(): AppCompatDelegate {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null)
        }
        return mDelegate as AppCompatDelegate
    }
}
