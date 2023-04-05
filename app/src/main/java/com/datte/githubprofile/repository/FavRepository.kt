package com.datte.githubprofile.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.datte.githubprofile.User
import com.datte.githubprofile.database.FavRoomDatabase
import com.datte.githubprofile.database.FavoriteUserDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFav(): LiveData<List<User>> = mFavoriteUserDao.getAllFav()

    fun insert(user: User) {
        executorService.execute { mFavoriteUserDao.insert(user)}
    }

    fun delete(user: User) {
        executorService.execute { mFavoriteUserDao.delete(user)}
    }
}