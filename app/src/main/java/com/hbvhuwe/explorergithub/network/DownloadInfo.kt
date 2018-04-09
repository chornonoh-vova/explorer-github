package com.hbvhuwe.explorergithub.network

import android.os.AsyncTask
import java.net.URL

interface LoadInfo {
    fun onLoadInfoCallback(tag: Tags, result: String?)
    fun onErrorCallback(tag: Tags)
    enum class Tags {
        USER , FILES, REPOS, BRANCHES, COMMITS
    }
}

class DownloadInfo(private val callback: LoadInfo, private val tag: LoadInfo.Tags): AsyncTask<String, Void, String>() {
    private var ex: Exception? = null
    override fun doInBackground(vararg params: String?): String {
        return try {
            val url = URL(params[0])
            val inputStream = url.openStream()
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            ex = e
            ""
        }
    }

    override fun onPostExecute(result: String?) {
        if (ex != null) {
            callback.onErrorCallback(tag)
        } else {
            callback.onLoadInfoCallback(tag, result)
        }
    }
}