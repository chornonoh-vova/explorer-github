package com.hbvhuwe.explorergithub.model

import java.io.Serializable
import java.net.URL

data class GitHubCommit(
        val sha: String,
        val url: URL,
        val author: User?,
        val committer: User?,
        val parents: Array<GitHubCommit?>
) : Serializable