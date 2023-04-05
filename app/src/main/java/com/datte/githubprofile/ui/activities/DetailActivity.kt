package com.datte.githubprofile.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.datte.githubprofile.DetailUser
import com.datte.githubprofile.R
import com.datte.githubprofile.User
import com.datte.githubprofile.adapter.SectionsPagerAdapter
import com.datte.githubprofile.databinding.ActivityDetailBinding
import com.datte.githubprofile.model.DetailViewModel
import com.datte.githubprofile.model.FavoriteViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.StringBuilder

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private var isFav: Boolean = false

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        val detailViewModel = obtainViewModel(this)
        val user = intent.getParcelableExtra<User>(EXTRA_USERNAME) as User

        detailViewModel.userlogin = user.login

        detailViewModel.detailUser.observe(this) {
            setDetailUser(it)
        }

        detailViewModel.getAllFav().observe(this) {
            isFav = it.contains(user)
            if (isFav) {
                binding.favBtn.text = resources.getText(R.string.favorite_x)
            } else {
                binding.favBtn.text = resources.getText(R.string.favorite)
            }
        }

        detailViewModel.isLoadingDetail.observe(this) {
            showLoading(it)
        }

        binding.favBtn.setOnClickListener {
            if (isFav) {
                detailViewModel.delete(user)
                binding.favBtn.text = resources.getText(R.string.favorite_x)
                Snackbar.make(
                    binding.root,
                    StringBuilder(user.login + " ").append(resources.getString(R.string.delete_fav)),
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else {
                detailViewModel.insert(user)
                binding.favBtn.text = resources.getText(R.string.favorite)
                Snackbar.make(
                    binding.root,
                    StringBuilder(user.login + " ").append(resources.getString(R.string.add_fav)),
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) { binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE }

    private fun setDetailUser(userItem: DetailUser) {
        binding.apply {
            Glide.with(applicationContext)
                .load(userItem.avatarUrl)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(imgAvatar)
            tvDetailName.text = userItem.name
            tvDetailUsername.text = userItem.login
            tvDetailRepo.text = userItem.publicRepos
            tvDetailFollowers.text = userItem.followers
            tvDetailFollowing.text = userItem.following
            val actionBar = supportActionBar
            actionBar!!.title = userItem.login

        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }
}