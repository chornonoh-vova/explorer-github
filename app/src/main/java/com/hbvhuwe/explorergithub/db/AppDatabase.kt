package com.hbvhuwe.explorergithub.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.hbvhuwe.explorergithub.models.User

@Database(entities = [(User::class)], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
