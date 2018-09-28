package com.hbvhuwe.explorergithub.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.model.User
import com.hbvhuwe.explorergithub.net.Api
import com.hbvhuwe.explorergithub.net.Credentials
import okhttp3.*
import java.io.IOException
import javax.inject.Inject

import retrofit2.Call as RCall
import retrofit2.Callback as RCallback

class LoginActivity : AppCompatActivity() {
    private val clientId by lazy { getString(R.string.application_client_id) }
    private val clientSecret by lazy { getString(R.string.application_client_secret) }
    private val redirectUri by lazy { getString(R.string.application_redirect_uri) }

    private val loginButton by lazy { findViewById<Button>(R.id.login_button) }
    private val loginResult by lazy { findViewById<TextView>(R.id.login_result) }

    private val authUrl = "https://github.com/login/oauth/authorize"
    private val tokenUrl = "https://github.com/login/oauth/access_token"

    @Inject lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            val httpUrl: HttpUrl = HttpUrl.parse(authUrl)!!
                    .newBuilder()
                    .addQueryParameter("client_id", clientId)
                    .build()

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl.toString()))
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val data = intent.data

        if (data != null && data.toString().startsWith(redirectUri)) {

            val code = data.getQueryParameter("code")
            if (code != null) {
                val request = Request.Builder()
                        .url(tokenUrl)
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
                            val value = TypedValue()
                            this@LoginActivity.theme.resolveAttribute(R.attr.colorAccent, value, true)
                            loginResult.setTextColor(value.data)
                            loginResult.text = getString(R.string.activity_login_logged)

                            val authCredentials = GsonBuilder().create().fromJson(responseText, Credentials::class.java)

                            (application as App).saveCredentials(authCredentials)

                            App.netComponent = (application as App).createNetComponent()
                            App.netComponent.inject(this@LoginActivity)
                            api.getUserInfo().enqueue(callback)
                        }
                    }

                })
            }
        }
    }

    private val callback = object : RCallback<User> {
        override fun onFailure(call: RCall<User>?, t: Throwable?) {

        }

        override fun onResponse(call: RCall<User>?, response: retrofit2.Response<User>?) {
            if (response != null) {
                val user = response.body()!!
                (application as App).saveUserLogin(user.login)

                intent = Intent(this@LoginActivity, UserActivity::class.java)
                intent.putExtra(Const.USER_KEY, Const.USER_LOGGED_IN)
                startActivity(intent)
                finish()
            }
        }

    }
}
