package com.hbvhuwe.explorergithub.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.hbvhuwe.explorergithub.model.User

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    fun save(user: User)

    @Query("select * from User where login = :login")
    fun load(login: String): LiveData<User>
}