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
        app: App
): BaseRepository(app) {

    fun getUser(login: String): LiveData<User> {
        return if (login == Const.USER_LOGGED_IN) {
            enqueueCall(api.getUserInfo())
        } else {
            enqueueCall(api.getUserInfo(login))
        }
    }

    fun getUserFollowers(login: String): LiveData<List<User>> {
        return if (login == Const.USER_LOGGED_IN) {
            enqueueCall(api.getUserFollowers())
        } else {
            enqueueCall(api.getUserFollowers(login))
        }
    }

    fun getUserFollowing(login: String): LiveData<List<User>> {
        return if (login == Const.USER_LOGGED_IN) {
            enqueueCall(api.getUserFollowing())
        } else {
            enqueueCall(api.getUserFollowing(login))
        }
    }

    fun getContributors(login: String, repo: String): LiveData<List<User>> =
            enqueueCall(api.getContributors(login, repo))

    private fun <T> enqueueCall(call: Call<T>) = MutableLiveData<T>().apply {
        call.enqueue(callback {
            this.value = it.body()
        })
    }
}