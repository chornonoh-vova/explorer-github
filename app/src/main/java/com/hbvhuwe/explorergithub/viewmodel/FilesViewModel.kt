package com.hbvhuwe.explorergithub.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private var initialized = false

    fun init(
            user: String,
            repo: String,
            branch: String = "master") {
        if (!initialized) {
            this.user = user
            this.repo = repo
            this.branch = branch
            initialized = true
        }
    }

    fun getFiles() = filesRepository.getFiles(user, repo, currentPath.value, branch)

    fun getBranches() = filesRepository.getBranches(user, repo)

    fun setCurrentPath(path: String) {
        this.currentPath.value = path
    }
}