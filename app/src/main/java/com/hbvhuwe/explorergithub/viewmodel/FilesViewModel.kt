package com.hbvhuwe.explorergithub.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hbvhuwe.explorergithub.repository.FilesRepository
import javax.inject.Inject

class FilesViewModel: ViewModel() {
    @Inject lateinit var filesRepository: FilesRepository

    val currentPath by lazy {
        MutableLiveData<String>()
    }
    lateinit var user: String
    lateinit var repo: String
    lateinit var branch: String

    fun init(
            user: String,
            repo: String,
            branch: String = "master") {
        if (!this::user::isInitialized.get()) {
            this.user = user
        }
        if (!this::repo::isInitialized.get()) {
            this.repo = repo
        }
        if (!this::branch::isInitialized.get()) {
            this.branch = branch
        }

    }

    fun getFiles() = filesRepository.getFiles(user, repo, currentPath.value!!, branch)

    fun setCurrentPath(path: String) {
        this.currentPath.value = path
    }
}