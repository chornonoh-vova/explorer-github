package com.hbvhuwe.explorergithub.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.net.Api
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class FileRepository @Inject constructor(
        private val api: Api,
        private val app: App
) {
    fun getFile(downloadUrl: String): LiveData<String> {
        val file = MutableLiveData<String>()
        val callback = object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                app.showNetworkError()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val fileContent = response.body()!!.string()
                    file.value = fileContent
                } else {
                    file.value = "Unable to get file"
                }
            }
        }
        api.getFile(downloadUrl).enqueue(callback)
        return file
    }
}