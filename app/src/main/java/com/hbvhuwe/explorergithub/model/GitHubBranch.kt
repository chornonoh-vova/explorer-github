package com.hbvhuwe.explorergithub.model

import java.io.Serializable

data class GitHubBranch(
        val name: String,
        val commit: GitHubCommit
) : Serializable