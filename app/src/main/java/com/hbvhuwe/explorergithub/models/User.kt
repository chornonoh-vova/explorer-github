package com.hbvhuwe.explorergithub.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.net.URL

@Entity
data class User(
        var login: String,
        var name: String,
        @PrimaryKey
        var id: Int,
        @Ignore @SerializedName("avatar_url") var avatarUrl: URL?,
        @Ignore @SerializedName("url") var userUrl: URL?,
        @Ignore @SerializedName("repos_url") var reposUrl: URL?,
        var type: String,
        var location: String,
        var company: String?,
        var email: String?,
        var bio: String?,
        @SerializedName("public_repos") var publicRepos: Int
) : Serializable {
    constructor() : this("", "", 0, null, null, null,
            "", "", null, null, null, 0)
}