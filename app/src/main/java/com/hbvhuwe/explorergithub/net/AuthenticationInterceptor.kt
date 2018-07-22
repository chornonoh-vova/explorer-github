package com.hbvhuwe.explorergithub.net

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthenticationInterceptor(private val credentials: Credentials?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response {
        val original = chain!!.request()

        val builder = original.newBuilder()
                .addHeader("Authorization", credentials.toString())

        val request = builder.build()
        return chain.proceed(request)
    }
}