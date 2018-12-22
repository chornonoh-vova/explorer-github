package com.hbvhuwe.explorergithub.repository

import com.hbvhuwe.explorergithub.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Common functions for all repositories, depends on [App]
 */
open class BaseRepository(val app: App) {

    /**
     * Generalized callback for network data retrieving
     *
     * @param onSuccess function, that will be called if response were successful
     */
    protected fun <T> callback(onSuccess: (response: Response<T>) -> Unit): Callback<T> {
        return object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                // need to check, if response were successful
                if (response.isSuccessful) {
                    onSuccess(response)
                } else {
                    app.showToast(response.errorBody().toString())
                }
            }

        }
    }

    /**
     * Get next page number from Link header from API
     *
     * @param response Retrofit response of any type(because only headers got)
     *
     * @return number of next page or -1 if none found
     */
    protected fun getNextPageNumber(response: Response<*>): Int {
        // checking header
        val links = response.headers().get("Link") ?: return -1

        val regex = "([0-9]*)>; rel=\"next\"".toRegex()

        val match = regex.find(links)

        return if (match?.destructured != null) match.destructured.component1().toInt() else -1
    }
}