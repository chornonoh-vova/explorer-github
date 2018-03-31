package com.hbvhuwe.explorergithub.models

import com.google.gson.annotations.SerializedName
import java.net.URL

data class GitHubFile(
        val name: String,
        val path: String,
        @SerializedName("download_url") val downloadUrl: URL?,
        val type: String
)
