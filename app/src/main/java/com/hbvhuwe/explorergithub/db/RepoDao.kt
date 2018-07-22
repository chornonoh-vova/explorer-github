package com.hbvhuwe.explorergithub.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.hbvhuwe.explorergithub.models.Repo

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(repo: Repo)

    @Query("select * from Repo where owner_login = :login")
    fun load(login: String): LiveData<List<Repo>>
}