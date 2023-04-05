package com.datte.githubprofile.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.datte.githubprofile.User
import com.datte.githubprofile.repository.FavRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mUserFavRepository: FavRepository = FavRepository(application)
    fun getAllFav(): LiveData<List<User>> = mUserFavRepository.getAllFav()
}