package com.hbvhuwe.explorergithub.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.models.Repo
import com.hbvhuwe.explorergithub.repository.RepoRepository
import javax.inject.Inject

class ReposViewModel : ViewModel() {
    @Inject lateinit var repository: RepoRepository

    private var repos: LiveData<List<Repo>>? = null

    fun init(mode: Int, login: String) {
        if (repos != null) {
            return
        }

        repos = if (mode == Const.REPOS_MODE_REPOS) {
            repository.getRepos(login)
        } else {
            repository.getStarredRepos(login)
        }
    }

    fun getRepos(): LiveData<List<Repo>>? = this.repos
}