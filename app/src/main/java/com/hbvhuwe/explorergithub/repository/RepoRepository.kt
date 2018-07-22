package com.hbvhuwe.explorergithub.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
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
        private val executor: ExecutorService
) {
    fun getRepos(login: String): LiveData<List<Repo>> {
        val repos = MutableLiveData<List<Repo>>()
        executor.execute {
            if (login == Const.LOGGED_IN_KEY) {
                repos.postValue(api.getReposForUser().execute().body())
            } else {
                repos.postValue(api.getReposForUser(login).execute().body())
            }
        }
        return repos
    }

    fun getStarredRepos(login: String): LiveData<List<Repo>> {
        val repos = MutableLiveData<List<Repo>>()
        val callback = object : Callback<List<Repo>> {
            override fun onFailure(call: Call<List<Repo>>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<List<Repo>>?, response: Response<List<Repo>>?) {
                repos.value = response?.body()
            }
        }
        if (login == Const.LOGGED_IN_KEY) {
            api.getStarredRepos().enqueue(callback)
        } else {
            api.getStarredRepos(login).enqueue(callback)
        }
        return repos
    }
}