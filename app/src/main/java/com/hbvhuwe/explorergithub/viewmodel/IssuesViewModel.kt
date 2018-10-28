package com.hbvhuwe.explorergithub.viewmodel

import androidx.lifecycle.ViewModel
import com.hbvhuwe.explorergithub.repository.IssuesRepository
import javax.inject.Inject

class IssuesViewModel: ViewModel() {
    @Inject lateinit var issuesRepository: IssuesRepository
    private lateinit var user: String
    private lateinit var repo: String

    fun init(user: String, repo: String) {
        this.user = user
        this.repo = repo
    }

    fun getIssues() = issuesRepository.getIssues(user, repo)
}
