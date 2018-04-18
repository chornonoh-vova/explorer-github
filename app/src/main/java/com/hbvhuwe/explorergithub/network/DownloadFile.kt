package com.hbvhuwe.explorergithub.network

import android.os.AsyncTask
import java.net.URL

interface LoadInfo {
    fun onLoadInfoCallback(result: String?)
    fun onErrorCallback()
}

class DownloadFile(private val callback: LoadInfo) : AsyncTask<String, Void, String>() {
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
            callback.onErrorCallback()
        } else {
            callback.onLoadInfoCallback(result)
        }
    }
}