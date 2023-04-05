package com.datte.githubprofile.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.datte.githubprofile.User

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * from User ORDER BY login ASC")
    fun getAllFav(): LiveData<List<User>>
}