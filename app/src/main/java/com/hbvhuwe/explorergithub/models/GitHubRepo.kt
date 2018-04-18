package com.hbvhuwe.explorergithub.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.net.URL

data class GitHubRepo(
        val id: Int,
        val owner: GitHubUser,
        val name: String,
        @SerializedName("full_name") val fullName: String,
        @SerializedName("contents_url") val contentsURL: URL,
        val description: String?,
        val homepage: String?,
        val language: String?,
        @SerializedName("stargazers_count") val starsCount: Int,
        val fork: Boolean
) : Serializable