package com.hbvhuwe.explorergithub.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.hbvhuwe.explorergithub.models.User
import com.hbvhuwe.explorergithub.repository.UserRepository
import javax.inject.Inject

class UserViewModel : ViewModel() {
    @Inject lateinit var repository: UserRepository

    private var user: LiveData<User>? = null

    fun init(login: String) {
        if (user != null) {
            return
        }
        user = repository.getUser(login)
    }

    fun getUser(): LiveData<User>? {
        return user
    }

}