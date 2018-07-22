package com.hbvhuwe.explorergithub.di

import com.hbvhuwe.explorergithub.App
import dagger.Module
import dagger.Provides
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class AppModule(private val mApp: App) {
    @Provides
    @Singleton
    fun provideApp() = mApp

    @Provides
    fun provideExecutorService(): ExecutorService = Executors.newSingleThreadExecutor()
}