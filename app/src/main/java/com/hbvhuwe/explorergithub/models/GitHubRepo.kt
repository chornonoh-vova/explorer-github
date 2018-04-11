package com.hbvhuwe.explorergithub.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GitHubRepo(
        val id: Int,
        val owner: GitHubUser,
        val name: String,
        @SerializedName("full_name") val fullName: String,
        val description: String?,
        val homepage: String?,
        val language: String?,
        @SerializedName("stargazers_count") val starsCount: Int,
        val fork: Boolean
) : Serializable