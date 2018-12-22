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
        app: App
) : BaseRepository(app) {
    private var nextPage = 1

    fun getIssues(user: String, repo: String): LiveData<List<Issue>> {
        val issues = MutableLiveData<List<Issue>>()

        val callback = callback<List<Issue>> { response ->
            nextPage = getNextPageNumber(response)
            val issuesFiltered = response.body()
            issues.value = issuesFiltered?.filter { it.pullRequest == null }
        }

        if (nextPage == -1) {
            return issues
        }
        api.getIssues(user, repo, page = nextPage).enqueue(callback)
        return issues
    }
}
