package com.hbvhuwe.explorergithub.network

import android.os.AsyncTask
import java.net.URL

interface LoadInfo {
    fun onLoadInfoCallback(result: String?)
}

class DownloadInfo(private val callback: LoadInfo): AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg params: String?): String {
        val url = URL(params[0])
        val inputStream = url.openStream()
        return inputStream.bufferedReader().use { it.readText() }
    }

    override fun onPostExecute(result: String?) {
        callback.onLoadInfoCallback(result)
    }
}