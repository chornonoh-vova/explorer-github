package com.hbvhuwe.explorergithub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.model.User
import com.hbvhuwe.explorergithub.repository.UserRepository
import javax.inject.Inject

class UserViewModel : ViewModel() {
    @Inject lateinit var repository: UserRepository

    private var user: LiveData<User>? = null

    private var users: LiveData<List<User>>? = null

    fun singleInit(login: String) {
        if (user != null) {
            return
        }
        user = repository.getUser(login)
    }

    fun multipleInit(mode: Int, login: String) {
        if (users != null) {
            return
        }
        users = if (mode == Const.USERS_MODE_FOLLOWERS) {
            repository.getUserFollowers(login)
        } else {
            repository.getUserFollowing(login)
        }
    }

    fun getUser(): LiveData<User>? = user

    fun getUsers(): LiveData<List<User>>? = users
}