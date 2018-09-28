package com.hbvhuwe.explorergithub.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hbvhuwe.explorergithub.model.Repo

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(repo: Repo)

    @Query("select * from Repo where owner_login = :login")
    fun load(login: String): LiveData<List<Repo>>

    @Query("select * from Repo where name = :name and owner_login = :login")
    fun load(login: String, name: String): LiveData<Repo>
}