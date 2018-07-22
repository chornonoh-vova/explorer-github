package com.hbvhuwe.explorergithub

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.hbvhuwe.explorergithub.net.Credentials
import okhttp3.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private val clientId by lazy { getString(R.string.application_client_id) }
    private val clientSecret by lazy { getString(R.string.application_client_secret) }
    private val redirectUri = "login://com.hbvhuwe.explorergithub"

    private val loginButton by lazy { findViewById<Button>(R.id.login_button) }
    private val loginResult by lazy { findViewById<TextView>(R.id.login_result) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            val httpUrl: HttpUrl = HttpUrl.parse("https://github.com/login/oauth/authorize")!!
                    .newBuilder()
                    .addQueryParameter("client_id", clientId)
                    .build()

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl.toString()))
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        if (intent.data != null && intent.data.toString().startsWith(redirectUri)) {

            val code = intent.data.getQueryParameter("code")

            val request = Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .header("Accept", "application/json")
                    .post(FormBody.Builder()
                            .add("client_id", clientId)
                            .add("client_secret", clientSecret)
                            .add("code", code)
                            .build())
                    .build()

            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    runOnUiThread {
                        loginResult.setTextColor(Color.RED)
                        loginResult.text = getString(R.string.activity_login_error)
                    }
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (response != null && response.isSuccessful) runOnUiThread {
                        val responseText = response.body()?.string()

                        loginResult.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.colorAccent))
                        loginResult.text = getString(R.string.activity_login_logged)

                        val authCredentials = GsonBuilder().create().fromJson(responseText, Credentials::class.java)

                        (application as App).saveCredentials(authCredentials)

                        intent = Intent(this@LoginActivity, UserActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

            })
        }
    }
}
