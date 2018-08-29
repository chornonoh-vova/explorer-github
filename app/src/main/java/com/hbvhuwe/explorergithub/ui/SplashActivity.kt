package com.hbvhuwe.explorergithub.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val credentials = (application as App).loadCredentials()

        if (!credentials.isEmpty()) {
            (application as App).loadUserLogin()

            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra(Const.USER_KEY, Const.USER_LOGGED_IN)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
