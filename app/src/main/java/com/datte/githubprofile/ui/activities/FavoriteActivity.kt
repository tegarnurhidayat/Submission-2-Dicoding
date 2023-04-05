package com.datte.githubprofile.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.datte.githubprofile.R
import com.datte.githubprofile.User
import com.datte.githubprofile.adapter.ListUserAdapter
import com.datte.githubprofile.databinding.ActivityFavoriteBinding
import com.datte.githubprofile.model.FavoriteViewModel
import com.datte.githubprofile.model.FavoriteViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private var _activityFavBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavBinding

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        _activityFavBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.rvFavUser?.layoutManager = LinearLayoutManager(this)
        binding?.rvFavUser?.setHasFixedSize(true)

        val favoriteViewModel = obtainViewModel(this)

        favoriteViewModel.getAllFav().observe(this) { users ->
            if (users != null) {
                setUser(users)
            }
        }

        supportActionBar?.title = getString(R.string.favorite_title)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun setUser(list: List<User>) {
        val listUserAdapter = ListUserAdapter(list)
        binding?.rvFavUser?.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                sendSelectedUser(data)
            }
        })
    }

    private fun sendSelectedUser(user: User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USERNAME, user)
        startActivity(intent)
    }
}