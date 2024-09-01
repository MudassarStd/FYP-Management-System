package com.android.cuifypmanagementsystem.teacher.adapter.pager

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.cuifypmanagementsystem.teacher.activities.mygroups.current.CurrentGroupsFragment
import com.android.cuifypmanagementsystem.teacher.activities.mygroups.requests.GroupRequestFragment

class TeacherGroupsAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2 // Number of pages

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentGroupsFragment()
            1 -> GroupRequestFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}
