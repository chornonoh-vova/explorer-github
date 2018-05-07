package com.hbvhuwe.explorergithub.models

import java.io.Serializable
import java.net.URL

data class GitHubCommit(
        val sha: String,
        val url: URL,
        val author: GitHubUser?,
        val committer: GitHubUser?,
        val parents: Array<GitHubCommit?>
) : Serializable