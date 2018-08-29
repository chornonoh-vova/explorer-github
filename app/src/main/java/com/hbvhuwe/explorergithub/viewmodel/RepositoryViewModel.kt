package com.hbvhuwe.explorergithub.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.hbvhuwe.explorergithub.model.GitHubFile
import com.hbvhuwe.explorergithub.model.Repo
import com.hbvhuwe.explorergithub.repository.RepoRepository
import javax.inject.Inject

class RepositoryViewModel: ViewModel() {
    @Inject lateinit var repository: RepoRepository

    private var repo: LiveData<Repo>? = null

    private var markdown: LiveData<String>? = null
    private var readme: LiveData<GitHubFile>? = null
    private var readmeHtml: LiveData<String>? = null

    fun init(login: String, repo: String) {
        if (this.repo != null)
            return

        this.repo = repository.getRepo(login, repo)
    }

    fun getRepo() = repo

    fun getReadme(login: String, repo: String): LiveData<GitHubFile>? {
        this.readme = repository.getReadme(login, repo)
        return readme
    }

    fun getFile(file: GitHubFile): LiveData<String>? {
        this.markdown = repository.getFileEscaping(file.downloadUrl.toString())
        return markdown
    }

    fun getReadmeHtml(markdown: String): LiveData<String>? {
        this.readmeHtml = repository.convertMarkdown(markdown)
        return readmeHtml
    }
}