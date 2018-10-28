package com.hbvhuwe.explorergithub.viewmodel

import androidx.lifecycle.ViewModel
import com.hbvhuwe.explorergithub.repository.FileRepository
import javax.inject.Inject

class FileViewModel: ViewModel() {
    @Inject lateinit var fileRepository: FileRepository

    private lateinit var url: String

    fun init(url: String) {
        this.url = url
    }

    fun getFile() = fileRepository.getFile(this.url)
}