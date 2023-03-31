package com.datte.githubprofile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datte.githubprofile.User
import com.datte.githubprofile.adapter.ListUserAdapter
import com.datte.githubprofile.databinding.FragmentFollowingBinding
import com.datte.githubprofile.model.DetailViewModel

class FollowingFragment : Fragment() {
    private val detailViewModel by viewModels<DetailViewModel>()
    private var binding: FragmentFollowingBinding? = null
    private val followingBinding get() = binding!!
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return followingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(ARG_USERNAME)

        val layoutManager = LinearLayoutManager(requireActivity())
        followingBinding.rvFollowing.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        followingBinding.rvFollowing.addItemDecoration(itemDecoration)

        detailViewModel.getFollowing(username)

        detailViewModel.followingUser.observe(viewLifecycleOwner) { user ->
            followingBinding.rvFollowing.adapter = showFragmentRecycler(user)
        }

        detailViewModel.isLoadingFollowing.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showFragmentRecycler(items: List<User>): ListUserAdapter {
        val listUsers = ArrayList<User>()
        followingBinding.itemFollowing.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE

        items.let {
            listUsers.addAll(it)
        }

        return ListUserAdapter(listUsers)
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            followingBinding.progressBarFollowing.visibility = View.VISIBLE
        } else {
            followingBinding.progressBarFollowing.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val ARG_USERNAME = "arg_username"
    }
}