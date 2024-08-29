package com.android.cuifypmanagementsystem.teacher.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.cuifypmanagementsystem.teacher.CurrentGroupsFragment
import com.android.cuifypmanagementsystem.teacher.GroupRequestFragment

class TeacherGroupsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // Number of pages

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentGroupsFragment()
            1 -> GroupRequestFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}
