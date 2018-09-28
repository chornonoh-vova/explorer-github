package com.hbvhuwe.explorergithub.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.net.URL

@Entity(
        indices = [
            Index("id"),
            Index("owner_login")],
        primaryKeys = ["name", "owner_login"]
)
data class Repo(
        val id: Int,
        @field:SerializedName("name")
        val name: String,
        @field:SerializedName("full_name")
        val fullName: String,
        @field:SerializedName("description")
        val description: String?,
        @field:SerializedName("owner")
        @Embedded(prefix = "owner_")
        val owner: User,
        @field:SerializedName("stargazers_count")
        val starsCount: Int,
        @field:SerializedName("contents_url")
        val contentsURL: URL?,
        val language: String
) : Serializable