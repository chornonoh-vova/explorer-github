package com.hbvhuwe.explorergithub.model

import com.google.gson.annotations.SerializedName
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.binaries
import com.hbvhuwe.explorergithub.pictures
import java.io.Serializable
import java.net.URL

data class File(
        val name: String,
        val path: String,
        val url: URL,
        @SerializedName("download_url") val downloadUrl: URL?,
        val type: String
) : Serializable {
    fun isRegularFile() = this.type == Const.FILE_TYPE_FILE
    fun isDirectory() = this.type == Const.FILE_TYPE_DIR
    fun isPicture() = this.type == Const.FILE_TYPE_FILE && this.getExtension() in pictures
    fun isBinary() = this.type == Const.FILE_TYPE_FILE && this.getExtension() in binaries

    private fun getExtension(): String {
        val l = this.name.lastIndexOf('.')
        return if (l > 0) {
            this.name.substring(l + 1)
        } else {
            ""
        }
    }
}
