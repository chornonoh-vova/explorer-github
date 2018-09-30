package com.hbvhuwe.explorergithub.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.model.Branch
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
            path: String?,
            branch: String = "master"
    ): LiveData<List<File>> {
        val usedPath = path ?: ""
        val files = MutableLiveData<List<File>>()
        val callback = object : Callback<List<File>> {
            override fun onFailure(call: Call<List<File>>, t: Throwable) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<List<File>>, response: Response<List<File>>) {
                if (response.isSuccessful) {
                    val filesResponse = response.body()!!.toMutableList()
                    filesResponse.sortWith(Comparator { o1, o2 ->
                        o1.type.compareTo(o2.type)
                    })
                    files.value = filesResponse
                } else {
                    app.showNetworkError()
                }
            }
        }
        api.getContentOfPath(user, repo, usedPath, branch).enqueue(callback)
        return files
    }

    fun getBranches(user: String, repo: String): LiveData<List<Branch>> {
        val branches = MutableLiveData<List<Branch>>()
        val callback = object : Callback<List<Branch>> {
            override fun onFailure(call: Call<List<Branch>>, t: Throwable) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<List<Branch>>, response: Response<List<Branch>>) {
                if (response.isSuccessful) {
                    branches.value = response.body()
                } else {
                    app.showNetworkError()
                }
            }

        }
        api.getBranchesForRepo(user, repo).enqueue(callback)
        return branches
    }
}