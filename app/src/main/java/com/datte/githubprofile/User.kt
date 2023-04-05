package com.datte.githubprofile
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class UserResponse(
    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @field:SerializedName("items")
    val items: List<User>
)
@Entity
@Parcelize
data class User(
    @PrimaryKey
    @ColumnInfo(name = "login")
    @field:SerializedName("login")
    val login: String,

    @ColumnInfo(name = "avatarUrl")
    @SerializedName("avatar_url")
    val avatarUrl: String
) :Parcelable

data class DetailUser(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("followers")
    val followers: String,

    @field:SerializedName("following")
    val following: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("public_repos")
    var publicRepos: String
)