package com.hbvhuwe.explorergithub.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.db.RepoDao
import com.hbvhuwe.explorergithub.models.Repo
import com.hbvhuwe.explorergithub.net.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import javax.inject.Inject

class RepoRepository @Inject constructor(
        private val api: Api,
        private val repoDao: RepoDao,
        private val executor: ExecutorService,
        private val app: App
) {
    fun getRepos(login: String): LiveData<List<Repo>> {
        var repos = MutableLiveData<List<Repo>>()
        val callback = object : Callback<List<Repo>> {
            override fun onFailure(call: Call<List<Repo>>?, t: Throwable?) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<List<Repo>>?, response: Response<List<Repo>>?) {
                repos.value = response?.body()
            }
        }
        if (login == Const.USER_LOGGED_IN) {
            api.getReposForUser().enqueue(callback)
        } else {
            repos = repoDao.load(login) as MutableLiveData<List<Repo>>
            api.getReposForUser(login).enqueue(callback)
            executeSave(repos)
        }
        return repos
    }

    fun getStarredRepos(login: String): LiveData<List<Repo>> {
        val repos = MutableLiveData<List<Repo>>()
        val callback = object : Callback<List<Repo>> {
            override fun onFailure(call: Call<List<Repo>>?, t: Throwable?) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<List<Repo>>?, response: Response<List<Repo>>?) {
                repos.value = response?.body()
            }
        }
        if (login == Const.USER_LOGGED_IN) {
            api.getStarredRepos().enqueue(callback)
        } else {
            api.getStarredRepos(login).enqueue(callback)
        }
        return repos
    }

    private fun executeSave(repos: LiveData<List<Repo>>) {
        executor.execute {
            repos.value?.forEach { repoDao.save(it) }
        }
    }
}