package com.hbvhuwe.explorergithub

import android.app.Application
import android.content.Context
import com.hbvhuwe.explorergithub.di.*
import com.hbvhuwe.explorergithub.net.Api
import com.hbvhuwe.explorergithub.net.AuthenticationInterceptor
import com.hbvhuwe.explorergithub.net.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    private val credentials: Credentials by lazy { loadCredentials() }

    fun createNetComponent(): NetComponent {
        return DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule(credentials))
                .dbModule(DbModule())
                .build()
    }

    fun loadCredentials(): Credentials {
        val preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        return if (preferences.getBoolean("logged", false)) {
            val accessToken = preferences.getString("accessToken", "")
            val tokenType = preferences.getString("tokenType", "")
            Credentials(accessToken, tokenType)
        } else {
            Credentials("", "");
        }
    }

    fun saveCredentials(credentials: Credentials) {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("logged", true)
            putString("accessToken", credentials.accessToken)
            putString("tokenType", credentials.tokenType)
            apply()
        }
    }

    companion object {
        private lateinit var retrofit: Retrofit
        private val httpClientBuilder = OkHttpClient.Builder()
        val api by lazy {
            createClient(access)
        }
        var access: Credentials? = null

        fun createClient(credentials: Credentials?): Api {
            access = credentials
            val authInterceptor = AuthenticationInterceptor(access)
            httpClientBuilder.addInterceptor(authInterceptor)
            retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClientBuilder.build())
                    .build()
            return retrofit.create(Api::class.java)
        }

        @JvmStatic lateinit var netComponent: NetComponent
    }
}