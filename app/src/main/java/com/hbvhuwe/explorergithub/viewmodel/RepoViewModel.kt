package com.hbvhuwe.explorergithub.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.model.Repo
import com.hbvhuwe.explorergithub.repository.RepoRepository
import javax.inject.Inject

class RepoViewModel : ViewModel() {
    @Inject lateinit var repository: RepoRepository

    private var repos: LiveData<List<Repo>>? = null

    private var repo: LiveData<Repo>? = null

    fun multipleInit(mode: Int, login: String) {
        if (repos != null) {
            return
        }

        repos = if (mode == Const.REPOS_MODE_REPOS) {
            repository.getRepos(login)
        } else {
            repository.getStarredRepos(login)
        }
    }

    fun singleInit(login: String, repoName: String) {
        if (repo != null) {
            return
        }
        repo = repository.getRepo(login, repoName)
    }

    fun getRepos(): LiveData<List<Repo>>? = this.repos

    fun getSingleRepo(): LiveData<Repo>? = this.repo
}