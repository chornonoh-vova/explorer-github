package com.hbvhuwe.explorergithub.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.model.File
import com.hbvhuwe.explorergithub.net.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class FilesRepository @Inject constructor(
        private val api: Api,
        private val app: App
) {
    fun getFiles(
            user: String,
            repo: String,
            path: String,
            branch: String = "master"
    ): LiveData<List<File>> {
        val files = MutableLiveData<List<File>>()
        val callback = object : Callback<List<File>> {
            override fun onFailure(call: Call<List<File>>, t: Throwable) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<List<File>>, response: Response<List<File>>) {
                if (response.isSuccessful) {
                    files.value = response.body()
                } else {
                    app.showNetworkError()
                }
            }
        }
        api.getContentOfPath(user, repo, path, branch).enqueue(callback)
        return files
    }
}