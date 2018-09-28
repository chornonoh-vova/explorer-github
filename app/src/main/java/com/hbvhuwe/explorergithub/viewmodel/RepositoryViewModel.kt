package com.hbvhuwe.explorergithub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hbvhuwe.explorergithub.model.File
import com.hbvhuwe.explorergithub.model.Repo
import com.hbvhuwe.explorergithub.repository.RepoRepository
import javax.inject.Inject

class RepositoryViewModel: ViewModel() {
    @Inject lateinit var repository: RepoRepository

    private var repo: LiveData<Repo>? = null

    private var markdown: LiveData<String>? = null
    private var readme: LiveData<File>? = null
    private var readmeHtml: LiveData<String>? = null

    val currentPath by lazy {
        MutableLiveData<String>()
    }

    fun init(login: String, repo: String) {
        if (this.repo != null)
            return
        this.repo = repository.getRepo(login, repo)
    }

    fun setCurrentPath(path: String) {
        currentPath.value = path
    }

    fun getRepo() = repo

    fun getReadme(login: String, repo: String): LiveData<File>? {
        this.readme = repository.getReadme(login, repo)
        return readme
    }

    fun getFile(file: File): LiveData<String>? {
        this.markdown = repository.getFileEscaping(file.downloadUrl.toString())
        return markdown
    }

    fun getReadmeHtml(markdown: String): LiveData<String>? {
        this.readmeHtml = repository.convertMarkdown(markdown)
        return readmeHtml
    }
}