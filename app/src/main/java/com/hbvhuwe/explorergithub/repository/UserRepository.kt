package com.hbvhuwe.explorergithub.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.db.UserDao
import com.hbvhuwe.explorergithub.models.User
import com.hbvhuwe.explorergithub.net.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
        private val api: Api,
        private val userDao: UserDao,
        private val executor: ExecutorService
) {

    fun getUser(login: String): LiveData<User> {
        return if (login == Const.LOGGED_IN_KEY) {
            getUser()
        } else {
            refreshUser(login)
            userDao.load(login)
        }
    }

    private fun getUser(): LiveData<User> {
        val loggedInUser: MutableLiveData<User> = MutableLiveData()

        api.getUserInfo().enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if (response != null) {
                    loggedInUser.value = response.body()
                }
            }

        })
        return loggedInUser
    }

    private fun refreshUser(login: String) {
        executor.execute {
            val user = api.getUserInfo(login).execute()
            user.body()?.let { userDao.save(it) }
        }
    }
}