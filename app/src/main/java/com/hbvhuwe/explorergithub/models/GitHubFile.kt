package com.hbvhuwe.explorergithub.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.net.URL

data class GitHubFile(
        val name: String,
        val path: String,
        val url: URL,
        @SerializedName("download_url") val downloadUrl: URL?,
        val type: String
) : Serializable
