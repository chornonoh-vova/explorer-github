package com.hbvhuwe.explorergithub.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.db.RepoDao
import com.hbvhuwe.explorergithub.model.Repo
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
            api.getReposForUser().enqueue(callback)
        } else {
            api.getReposForUser(login).enqueue(callback)
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
                if (response != null) {
                    repos.value = response.body()
                }
            }
        }
        if (login == Const.USER_LOGGED_IN) {
            api.getStarredRepos().enqueue(callback)
        } else {
            api.getStarredRepos(login).enqueue(callback)
        }
        return repos
    }

    fun getRepo(login: String, repoName: String): LiveData<Repo> {
        val repo = repoDao.load(login, repoName)
        val callback = object : Callback<Repo> {
            override fun onFailure(call: Call<Repo>?, t: Throwable?) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<Repo>?, response: Response<Repo>?) {
                if (response != null) {
                    executeSave(response.body()!!)
                }
            }
        }
        api.getRepo(login, repoName).enqueue(callback)
        return repo
    }

    private fun executeSave(repos: List<Repo>) {
        executor.execute {
            repos.forEach { repoDao.save(it) }
        }
    }

    private fun executeSave(repo: Repo) {
        executor.execute {
            repo.let { repoDao.save(it) }
        }
    }
}