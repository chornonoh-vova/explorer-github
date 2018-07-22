package com.hbvhuwe.explorergithub.di

import com.hbvhuwe.explorergithub.net.Api
import com.hbvhuwe.explorergithub.net.AuthenticationInterceptor
import com.hbvhuwe.explorergithub.net.Credentials
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetModule(private val credentials: Credentials) {
    private val API_BASE_URL = "https://api.github.com"

    @Provides
    @Singleton
    fun provideAuthInterceptor() = AuthenticationInterceptor(credentials)

    @Provides
    @Singleton
    fun provideHttpClient(authInterceptor: AuthenticationInterceptor): OkHttpClient =
            OkHttpClient().newBuilder()
                    .addInterceptor(authInterceptor)
                    .build()

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .baseUrl(API_BASE_URL)
                    .build()

    @Provides
    fun provideApi(retrofit: Retrofit): Api =
            retrofit.create(Api::class.java)

}