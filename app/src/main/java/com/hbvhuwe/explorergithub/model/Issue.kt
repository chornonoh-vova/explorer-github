package com.hbvhuwe.explorergithub.model

import com.google.gson.annotations.SerializedName

data class Issue (
        val url: String,
        val id: String,
        val number: Int,
        val title: String,
        val user: User,
        val state: String,
        val locked: Boolean,
        val assignee: User?,
        val comments: Int,
        val body: String,
        val labels: List<Label>,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("closed_at") val closedAt: String?,
        @SerializedName("pull_request") val pullRequest: Any?
)