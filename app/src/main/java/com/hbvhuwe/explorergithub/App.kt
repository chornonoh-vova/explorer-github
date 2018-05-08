package com.hbvhuwe.explorergithub

import android.app.Application
import com.hbvhuwe.explorergithub.network.AccessToken
import com.hbvhuwe.explorergithub.network.AuthenticationInterceptor
import com.hbvhuwe.explorergithub.network.GitHubApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    companion object {
        private lateinit var retrofit: Retrofit
        private val httpClientBuilder = OkHttpClient.Builder()
        lateinit var api: GitHubApi
        private lateinit var access: AccessToken

        fun createClient(accessToken: AccessToken) {
            access = accessToken
            val authInterceptor = AuthenticationInterceptor(access)
            httpClientBuilder.addInterceptor(authInterceptor)
            retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClientBuilder.build())
                    .build()
            api = retrofit.create(GitHubApi::class.java)
        }
    }
}