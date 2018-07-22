package com.hbvhuwe.explorergithub.repository

import android.arch.lifecycle.LiveData
import com.hbvhuwe.explorergithub.db.UserDao
import com.hbvhuwe.explorergithub.models.User
import com.hbvhuwe.explorergithub.net.Api
import java.util.concurrent.ExecutorService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository
@Inject constructor(val api: Api, val userDao: UserDao, val executor: ExecutorService) {

    fun getUser(login: String): LiveData<User> {
        refreshUser(login)
        return userDao.load(login)
    }

    private fun refreshUser(login: String) {
        executor.execute {
            val user = api.getUserInfo(login).execute()
            user.body()?.let { userDao.save(it) }
        }
    }
}