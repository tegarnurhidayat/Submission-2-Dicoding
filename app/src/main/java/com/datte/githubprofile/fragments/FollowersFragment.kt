package com.datte.githubprofile.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datte.githubprofile.User
import com.datte.githubprofile.adapter.ListUserAdapter
import com.datte.githubprofile.databinding.FragmentFollowersBinding
import com.datte.githubprofile.model.DetailViewModel
import com.datte.githubprofile.ui.activities.DetailActivity

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater,container,false)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFollowers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollowers.addItemDecoration(itemDecoration)

        val detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        detailViewModel.isLoadingFollower.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        detailViewModel.followersUser.observe(viewLifecycleOwner) {
            setUserFollowerData(it)
        }
        return binding.root
    }

    private fun setUserFollowerData(user: List<User>) {
        binding.itemFollowers.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
        binding.rvFollowers.apply {
            binding.rvFollowers.layoutManager = LinearLayoutManager(context)
            val listUserAdapter = ListUserAdapter(user)
            binding.rvFollowers.adapter = listUserAdapter

            listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: User) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USERNAME, data)
                    startActivity(intent)
                    activity?.finish()

                }
            })

        }
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBarFollower.visibility = View.VISIBLE
        } else {
            binding.progressBarFollower.visibility = View.INVISIBLE
        }
    }
}