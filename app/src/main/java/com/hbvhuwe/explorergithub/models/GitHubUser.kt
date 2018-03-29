package com.hbvhuwe.explorergithub.models

import com.google.gson.annotations.SerializedName
import java.net.URL

data class GitHubUser (
        val login: String,
        val name: String,
        val id: Int,
        @SerializedName("avatar_url") val avatarUrl: URL,
        @SerializedName("url") val userUrl: URL,
        @SerializedName("repos_url") val reposUrl: URL,
        val type: String,
        val location: String,
        val company: String?,
        val email: String?,
        val bio: String?,
        @SerializedName("public_repos") val publicRepos: Int
)