package com.datte.githubprofile.api

import com.datte.githubprofile.DetailUser
import com.datte.githubprofile.User
import com.datte.githubprofile.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getUserSearch(
        @Query("q") login: String,
    ): Call<UserResponse>

    @GET("users/{login}/followers")
    fun getAllUserFollower(
        @Path("login") login: String?,
    ): Call<List<User>>

    @GET("users/{login}/following")
    fun getAllUserFollowing(
        @Path("login") login: String?,
    ): Call<List<User>>

    @GET("users/{login}")
    fun getDetailUser(
        @Path("login") login: String?,
    ): Call<DetailUser>
}