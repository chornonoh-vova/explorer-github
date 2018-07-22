package com.hbvhuwe.explorergithub.di

import android.arch.persistence.room.Room
import com.hbvhuwe.explorergithub.App
import com.hbvhuwe.explorergithub.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DbModule {
    @Provides
    @Singleton
    fun provideDb(app: App) = Room.databaseBuilder(app,
            AppDatabase::class.java, "app.db").build()

    @Provides
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()
}