package com.hbvhuwe.explorergithub.net

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Credentials(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("token_type") val tokenType: String
): Serializable {
    fun isEmpty() = (accessToken.isEmpty() && tokenType.isEmpty())
    override fun toString() = "$tokenType $accessToken"
}