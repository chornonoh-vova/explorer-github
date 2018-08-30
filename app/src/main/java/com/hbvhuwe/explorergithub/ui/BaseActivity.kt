package com.hbvhuwe.explorergithub.ui

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import com.hbvhuwe.explorergithub.R

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPrefs.getBoolean("dark_theme", false))
            setTheme(R.style.AppThemeDark)
        else
            setTheme(R.style.AppThemeLight)

        super.onCreate(savedInstanceState)
    }
}