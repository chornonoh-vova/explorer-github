package com.hbvhuwe.explorergithub.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.Const
import com.hbvhuwe.explorergithub.db.UserDao
import com.hbvhuwe.explorergithub.model.User
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
        private val executor: ExecutorService,
        private val app: App
) {

    fun getUser(login: String): LiveData<User> {
        val user = MutableLiveData<User>()
        val callback = object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if (response != null) {
                    user.value = response.body()
                }
            }
        }

        if (login == Const.USER_LOGGED_IN) {
            api.getUserInfo().enqueue(callback)
        } else {
            api.getUserInfo(login).enqueue(callback)
            executor.execute {
                user.value?.let { userDao.save(it) }
            }
        }
        return user
    }

    fun getUserFollowers(login: String): LiveData<List<User>> {
        val users = MutableLiveData<List<User>>()
        val callback = object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                if (response != null) {
                    users.value = response.body()
                }
            }
        }
        if (login == Const.USER_LOGGED_IN) {
            api.getUserFollowers().enqueue(callback)
        } else {
            api.getUserFollowers(login).enqueue(callback)
        }
        return users
    }

    fun getUserFollowing(login: String): LiveData<List<User>> {
        val users = MutableLiveData<List<User>>()
        val callback = object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                if (response != null) {
                    users.value = response.body()
                }
            }
        }
        if (login == Const.USER_LOGGED_IN) {
            api.getUserFollowing().enqueue(callback)
        } else {
            api.getUserFollowing(login).enqueue(callback)
        }
        return users
    }
}