package com.hbvhuwe.explorergithub.network

import com.hbvhuwe.explorergithub.models.*
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import java.io.IOException
import java.io.Serializable

interface GitHubApi {
    @GET("/users/{user}/repos")
    fun getReposForUser(@Path("user") user: String): Call<List<GitHubRepo>>

    @GET("/user/repos")
    fun getReposForUser(): Call<List<GitHubRepo>>

    @GET("/user/starred")
    fun getStarredRepos(): Call<List<GitHubRepo>>

    @GET("/users/{user}/starred")
    fun getStarredRepos(@Path("user") user: String): Call<List<GitHubRepo>>

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

    @GET("/repos/{user}/{repo}/contents/{path}")
    fun getContentOfPath(
            @Path("user") user: String,
            @Path("repo") repo: String,
            @Path("path") path: String,
            @Query("ref") branch: String = "master"): Call<List<GitHubFile>>
    @GET
    fun getFile(@Url url: String): Call<ResponseBody>
}

class AuthenticationInterceptor(private val authToken: AccessToken?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response {
        val original = chain!!.request()

        val builder = original.newBuilder()
                .addHeader("Authorization", "Bearer ${authToken?.access_token}")

        val request = builder.build()
        return chain.proceed(request)
    }
}

data class AccessToken(
        val access_token: String,
        val token_type: String
): Serializable
