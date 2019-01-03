package com.hbvhuwe.explorergithub.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.MarkdownRequest
import com.hbvhuwe.explorergithub.db.RepoDao
import com.hbvhuwe.explorergithub.model.File
import com.hbvhuwe.explorergithub.model.Repo
import com.hbvhuwe.explorergithub.model.Topics
import com.hbvhuwe.explorergithub.net.Api
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import javax.inject.Inject

class RepoRepository @Inject constructor(
        private val api: Api,
        private val repoDao: RepoDao,
        private val executor: ExecutorService,
        app: App
): BaseRepository(app) {
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
//        val repo = repoDao.load(login, repoName)
        val repo = MutableLiveData<Repo>()
        val callback = object : Callback<Repo> {
            override fun onFailure(call: Call<Repo>?, t: Throwable?) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<Repo>?, response: Response<Repo>?) {
                if (response != null) {
                    repo.value = response.body()
                }
            }
        }
        api.getRepo(login, repoName).enqueue(callback)
        return repo
    }

    fun getTopics(login: String, repo: String): LiveData<Topics> {
        val topics = MutableLiveData<Topics>()
        val callback = callback<Topics> {
            topics.value = it.body()
        }
        api.getTopics(login, repo).enqueue(callback)
        return topics
    }

    fun getReadme(login: String, repo: String): LiveData<File> {
        val readme = MutableLiveData<File>()
        val callback = object : Callback<File> {
            override fun onFailure(call: Call<File>?, t: Throwable?) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<File>?, response: Response<File>?) {
                if (response != null) {
                    if (response.body() != null) {
                        readme.value = response.body()
                    }
                }
            }

        }
        api.getReadme(login, repo).enqueue(callback)
        return readme
    }

    fun convertMarkdown(text: String): LiveData<String> {
        val html = MutableLiveData<String>()
        val callback = object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response != null) {
                    html.value = response.body()?.string()
                }
            }
        }
        val req = MarkdownRequest(text, "gfm")
        val call = api.convertMarkdownToHtml(req)
        call.enqueue(callback)
        return html
    }

    fun getFileEscaping(url: String): LiveData<String> {
        val file = MutableLiveData<String>()
        val callback = callback<ResponseBody> { response ->
            file.value = response.body()?.string()
        }
        api.getFile(url).enqueue(callback)
        return file
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