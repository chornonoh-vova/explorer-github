package com.hbvhuwe.explorergithub.network

import android.text.TextUtils
import com.hbvhuwe.explorergithub.models.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException
import java.io.Serializable

interface GitHubClient {
    @GET("/users/{user}/repos")
    fun getReposForUser(@Path("user") user: String): Call<List<GitHubRepo>>

    @GET("/user/repos")
    fun getReposForUser(): Call<List<GitHubRepo>>

    @GET("/users/{user}")
    fun getUserInfo(@Path("user") user: String): Call<GitHubUser>

    @GET("/user")
    fun getUserInfo(): Call<GitHubUser>

    @GET("/repos/{user}/{repo}/branches")
    fun getBranchesForRepo(
            @Path("user") user: String,
            @Path("repo") repo: String): Call<List<GitHubBranch>>

    @GET("/repos/{user}/{repo}/commits")
    fun getCommitsOfRepo(
            @Path("user") user: String,
            @Path("repo") repo: String): Call<List<GitHubCommit>>

    @GET("/repos/{user}/{repo}/contents")
    fun getContentOfRepo(
            @Path("user") user: String,
            @Path("repo") repo: String): Call<List<GitHubFile>>

    @GET("/repos/{user}/{repos}/contents/{path}")
    fun getContentOfPath(
            @Path("user") user: String,
            @Path("repo") repo: String,
            @Path("path") path: String,
            @Query("ref") branch: String = "master"): Call<List<GitHubFile>>
}

object ServiceGenerator {
    private const val BASE_URL = "https://api.github.com/"
    @JvmStatic
    lateinit var authToken: AccessToken

    @JvmStatic
    private val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    @JvmStatic
    private val httpClient = OkHttpClient.Builder()

    @JvmStatic
    private lateinit var retrofit: Retrofit

    @JvmStatic
    fun <S> createService(serviceClass: Class<S>): S {
        if (!TextUtils.isEmpty(authToken.access_token)) {
            val interceptor = AuthenticationInterceptor(authToken)
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            if (!httpClient.interceptors().contains(interceptor) && !httpClient.interceptors().contains(logging)) {
                httpClient.addInterceptor(interceptor)
                httpClient.addInterceptor(logging)
                builder.client(httpClient.build())
                retrofit = builder.build()
            }
        }
        return retrofit.create(serviceClass)
    }
}

class AuthenticationInterceptor(private val authToken: AccessToken) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response {
        val original = chain!!.request()

        val builder = original.newBuilder()
                .addHeader("Authorization", "Bearer ${authToken.access_token}")

        val request = builder.build()
        return chain.proceed(request)
    }
}

data class AccessToken(
        val access_token: String,
        val token_type: String
): Serializable
