package com.hbvhuwe.explorergithub.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.hbvhuwe.explorergithub.model.Repo
import com.hbvhuwe.explorergithub.model.User

@Database(
        entities = [
            User::class,
            Repo::class],
        version = 3,
        exportSchema = false)
@TypeConverters(UrlConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repoDao(): RepoDao
}
