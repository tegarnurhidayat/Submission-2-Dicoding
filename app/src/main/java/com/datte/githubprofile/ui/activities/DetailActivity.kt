package com.datte.githubprofile.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.datte.githubprofile.DetailUser
import com.datte.githubprofile.R
import com.datte.githubprofile.adapter.SectionsPagerAdapter
import com.datte.githubprofile.databinding.ActivityDetailBinding
import com.datte.githubprofile.model.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        if (username != null) {
            detailViewModel.getUser(username)
        }

        detailViewModel.detailUser.observe(this) { item ->
            setDetailUser(item)
        }

        detailViewModel.isLoadingDetail.observe(this) {
            showLoading(it)
        }

        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
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