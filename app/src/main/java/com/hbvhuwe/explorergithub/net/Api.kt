package com.hbvhuwe.explorergithub.net

import com.hbvhuwe.explorergithub.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @GET("/users/{user}/repos?sort=updated")
    fun getReposForUser(@Path("user") user: String): Call<List<Repo>>

    @GET("/user/repos?sort=updated")
    fun getReposForUser(): Call<List<Repo>>

    @GET("/user/starred")
    fun getStarredRepos(): Call<List<Repo>>

    @GET("/users/{user}/starred")
    fun getStarredRepos(@Path("user") user: String): Call<List<Repo>>

    @GET("/users/{user}")
    fun getUserInfo(@Path("user") user: String): Call<User>

    @GET("/user")
    fun getUserInfo(): Call<User>

    @GET("/user/followers")
    fun getUserFollowers(): Call<List<User>>

    @GET("/user/following")
    fun getUserFollowing(): Call<List<User>>

    @GET("users/{user}/followers")
    fun getUserFollowers(@Path("user") user: String): Call<List<User>>

    @GET("users/{user}/following")
    fun getUserFollowing(@Path("user") user: String): Call<List<User>>

    @GET("/repos/{owner}/{repo}")
    fun getRepo(
            @Path("owner") owner: String,
            @Path("repo") repo: String
    ): Call<Repo>

    @GET("/repos/{user}/{repo}/branches")
    fun getBranchesForRepo(
            @Path("user") user: String,
            @Path("repo") repo: String
    ): Call<List<Branch>>

    @GET("/repos/{user}/{repo}/commits")
    fun getCommitsOfRepo(
            @Path("user") user: String,
            @Path("repo") repo: String
    ): Call<List<Commit>>

    @GET("/repos/{user}/{repo}/contents/{path}")
    fun getContentOfPath(
            @Path("user") user: String,
            @Path("repo") repo: String,
            @Path("path") path: String,
            @Query("ref") branch: String = "master"
    ): Call<List<File>>

    @GET("/repos/{user}/{repo}/readme")
    fun getReadme(
            @Path("user") user: String,
            @Path("repo") repo: String
    ): Call<File>

    @GET("/repos/{user}/{repo}/topics")
    fun getTopics(
            @Path("user") user: String,
            @Path("repo") repo: String,
            @Header("X-GitHub-Media-Type") header: String = "application/vnd.github.mercy-preview+json"
    ): Call<Topics>

    @POST("/markdown")
    fun convertMarkdownToHtml(@Body text: String): Call<ResponseBody>

    @GET
    fun getFile(@Url url: String): Call<ResponseBody>

    @GET("/repos/{user}/{repo}/issues?state=all")
    fun getIssues(
            @Path("user") user: String,
            @Path("repo") repo: String
    ): Call<List<Issue>>
}
