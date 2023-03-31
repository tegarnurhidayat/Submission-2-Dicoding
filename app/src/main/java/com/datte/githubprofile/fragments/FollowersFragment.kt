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
import com.datte.githubprofile.databinding.FragmentFollowersBinding
import com.datte.githubprofile.model.DetailViewModel

class FollowersFragment : Fragment() {

    private val detailViewModel by viewModels<DetailViewModel>()
    private var binding: FragmentFollowersBinding? = null
    private val followersBinding get() = binding!!
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return followersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(ARG_USERNAME)

        val layoutManager = LinearLayoutManager(requireActivity())
        followersBinding.rvFollowers.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        followersBinding.rvFollowers.addItemDecoration(itemDecoration)

        detailViewModel.getFollowers(username)

        detailViewModel.followersUser.observe(viewLifecycleOwner) { user ->
            followersBinding.rvFollowers.adapter = showFragmentRecycler(user)
        }

        detailViewModel.isLoadingFollower.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showFragmentRecycler(items: List<User>): ListUserAdapter {
        val listUsers = ArrayList<User>()
        followersBinding.itemFollowers.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE

        items.let {
            listUsers.addAll(it)
        }

        return ListUserAdapter(listUsers)
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            followersBinding.progressBarFollower.visibility = View.VISIBLE
        } else {
            followersBinding.progressBarFollower.visibility = View.INVISIBLE
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