package com.datte.githubprofile.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datte.githubprofile.R
import com.datte.githubprofile.User
import com.datte.githubprofile.adapter.ListUserAdapter
import com.datte.githubprofile.databinding.ActivityMainBinding
import com.datte.githubprofile.model.MainViewModel



val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private var dataUser = listOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        mainViewModel.user.observe(this) { users ->
            dataUser = users
            setUser(users)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.setting_button) {
            startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            return true
        } else if (id == R.id.favorite_button) {
            startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_button).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        mainViewModel.searchUser.observe(this) { query ->
            searchView.setQuery(query.toString(), false)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.getUserSearch(query)

                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                setUser(dataUser)
                return false
            }
        })
        return true
    }

    private fun setUser(list: List<User>) {
        val userList = ListUserAdapter(list)
        binding.rvUsers.adapter = userList

        userList.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                sendSelectedUser(data)
            }
        })
    }

    private fun sendSelectedUser(user: User) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USERNAME, user)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvUsers.alpha = 0.0F
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvUsers.alpha = 1F
        }
    }

}