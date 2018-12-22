package com.hbvhuwe.explorergithub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.hbvhuwe.explorergithub.model.Issue
import com.hbvhuwe.explorergithub.repository.IssuesRepository
import javax.inject.Inject

class IssuesViewModel: ViewModel() {
    @Inject lateinit var issuesRepository: IssuesRepository
    private lateinit var user: String
    private lateinit var repo: String
    private val issues = MutableLiveData<MutableList<Issue>>()

    fun init(user: String, repo: String) {
        this.user = user
        this.repo = repo
    }

    fun getIssues() = issuesRepository.getIssues(user, repo)

    fun getNextPage() = issuesRepository.getIssues(user, repo)
}
