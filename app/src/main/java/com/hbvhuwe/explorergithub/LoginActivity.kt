package com.hbvhuwe.explorergithub

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.hbvhuwe.explorergithub.network.AccessToken
import com.hbvhuwe.explorergithub.network.ServiceGenerator
import okhttp3.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private val clientId = "8f8927148244148aec37"
    private val clientSecret = "f1a07eb8b20e6769fa817918b6cb8da7860300e1"
    private val redirectUri = "login://com.hbvhuwe.explorergithub"

    private val loginButton by lazy { findViewById<Button>(R.id.login_button) }
    private val loginResult by lazy { findViewById<TextView>(R.id.login_result) }

    lateinit var authToken: AccessToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkLogin()

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
                        loginResult.text = "failure ${e?.message}"
                    }
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (response != null) {
                        if (response.isSuccessful) runOnUiThread {
                            val responseText = response.body()?.string()
                            loginResult.setTextColor(Color.GREEN)
                            loginResult.text = "successfully logged"
                            authToken = GsonBuilder().create().fromJson(responseText, AccessToken::class.java)
                            ServiceGenerator.authToken = authToken
                            val sharedPreferences = this@LoginActivity
                                    .getSharedPreferences("preferences", Context.MODE_PRIVATE)
                            with(sharedPreferences.edit()) {
                                putBoolean("logged", true)
                                putString("access_token", authToken.access_token)
                                putString("token_type", authToken.token_type)
                                apply()
                            }
                            println("ACCESS_TOKEN ${authToken.access_token}")
                            println("TOKEN_TYPE ${authToken.token_type}")
                            checkLogin()
                        }
                    }
                }

            })
        }
    }

    private fun checkLogin() {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("logged", false)) {
            val accessToken = sharedPreferences.getString("access_token", "")
            val tokenType = sharedPreferences.getString("type_token", "")
            ServiceGenerator.authToken = AccessToken(accessToken, tokenType)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
