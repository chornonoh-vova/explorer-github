package com.hbvhuwe.explorergithub.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.ui.fragments.SettingsFragment
import android.content.pm.PackageManager

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(findViewById(R.id.toolbar_settings))
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
                .replace(R.id.settingsFragment, SettingsFragment())
                .commit()

        val appVersion = findViewById<TextView>(R.id.app_version)
        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            val verCode = pInfo.versionCode
            appVersion.text = "Version: $version\nBuild: $verCode"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}
