package com.datte.githubprofile.model


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.datte.githubprofile.User
import com.datte.githubprofile.UserResponse
import com.datte.githubprofile.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainViewModel : ViewModel() {
    var filtered = mutableListOf<User>()
    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _user

    private val _searchUser = MutableLiveData<List<User>>()
    val searchUser: LiveData<List<User>> = _searchUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getUserSearch(DEFAULT_USERNAME)
    }

    private fun search() {
        filtered.clear()
        val filteredtext = newText.lowercase(Locale.getDefault())
        if (filteredtext.isNotEmpty()) {
            getUserSearch(filteredtext)
        }
    }

    fun getUserSearch(input: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getUserSearch(input)
        api.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        _user.value = response.body()?.items
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }



    var newText: String = ""
        set(value) {
            field = value
            search()
        }


    companion object {
        private const val TAG = "MainViewModel"
        private const val DEFAULT_USERNAME = "tegar"
    }
}