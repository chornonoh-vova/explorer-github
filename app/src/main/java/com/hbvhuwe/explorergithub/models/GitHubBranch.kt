package com.hbvhuwe.explorergithub.models

import java.io.Serializable

data class GitHubBranch(
        val name: String,
        val commit: GitHubCommit
) : Serializable