package com.hbvhuwe.explorergithub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hbvhuwe.explorergithub.network.AccessToken

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)

        val preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        if (preferences.getBoolean("logged", false)) {
            val accessToken = preferences.getString("access_token", "")
            val tokenType = preferences.getString("token_type", "")

            App.access = AccessToken(accessToken, tokenType)

            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
