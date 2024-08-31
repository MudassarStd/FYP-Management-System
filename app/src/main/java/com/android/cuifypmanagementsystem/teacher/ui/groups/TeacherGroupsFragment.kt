package com.android.cuifypmanagementsystem.teacher.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.databinding.FragmentTeacherGroupsBinding
import com.android.cuifypmanagementsystem.teacher.adapter.pager.TeacherGroupsAdapter
import com.google.android.material.tabs.TabLayoutMediator


class TeacherGroupsFragment : Fragment() {

    private var _binding: FragmentTeacherGroupsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(TeacherGroupsViewModel::class.java)

        _binding = FragmentTeacherGroupsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val tabLayout = binding.tabLayoutTeacherGroups
        val viewPager = binding.viewPagerTeacherGroups

        val pagerAdapter = TeacherGroupsAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Current"
                1 -> tab.text = "Requests"
            }
        }.attach()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}