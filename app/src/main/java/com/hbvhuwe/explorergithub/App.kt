package com.hbvhuwe.explorergithub

import android.app.Application
import com.hbvhuwe.explorergithub.network.GitHubClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    private lateinit var retrofit: Retrofit
    override fun onCreate() {
        super.onCreate()
        retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        client = retrofit.create(GitHubClient::class.java)
    }

    companion object {
        lateinit var client: GitHubClient
    }
}