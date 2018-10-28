package com.hbvhuwe.explorergithub.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.model.Issue
import com.hbvhuwe.explorergithub.net.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class IssuesRepository @Inject constructor(
        val api: Api,
        val app: App
){
    fun getIssues(user: String, repo: String): LiveData<List<Issue>> {
        val issues = MutableLiveData<List<Issue>>()
        val callback = object: Callback<List<Issue>> {
            override fun onFailure(call: Call<List<Issue>>, t: Throwable) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<List<Issue>>, response: Response<List<Issue>>) {
                if (response.isSuccessful) {
                    issues.value = response.body()
                }
            }
        }
        api.getIssues(user, repo).enqueue(callback)
        return issues
    }
}
