package com.datte.githubprofile.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.datte.githubprofile.SettingPreferences
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}