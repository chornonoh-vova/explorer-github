package com.hbvhuwe.explorergithub.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.hbvhuwe.explorergithub.App
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
                    user.postValue(response.body())
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
}