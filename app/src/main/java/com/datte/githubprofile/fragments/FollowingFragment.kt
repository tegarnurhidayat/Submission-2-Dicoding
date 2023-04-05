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
import com.datte.githubprofile.databinding.FragmentFollowingBinding
import com.datte.githubprofile.model.DetailViewModel
import com.datte.githubprofile.ui.activities.DetailActivity

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFollowing.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollowing.addItemDecoration(itemDecoration)

        val detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        detailViewModel.isLoadingFollowing.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        detailViewModel.followingUser.observe(viewLifecycleOwner) {
            setUserFollowerData(it)
        }

        return binding.root
    }

    private fun setUserFollowerData(user: List<User>) {
        binding.itemFollowing.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
        binding.rvFollowing.apply {
            binding.rvFollowing.layoutManager = LinearLayoutManager(context)
            val listUserAdapter = ListUserAdapter(user)
            binding.rvFollowing.adapter = listUserAdapter

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
            binding.progressBarFollowing.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowing.visibility = View.INVISIBLE
        }
    }
}