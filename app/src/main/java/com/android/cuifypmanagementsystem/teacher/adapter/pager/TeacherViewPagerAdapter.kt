package com.android.cuifypmanagementsystem.teacher.adapter.pager

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.cuifypmanagementsystem.teacher.ui.dashboard.DashboardFragment
import com.android.cuifypmanagementsystem.teacher.ui.groups.TeacherGroupsFragment
import com.android.cuifypmanagementsystem.teacher.ui.notifications.NotificationsFragment
import com.android.cuifypmanagementsystem.teacher.ui.profile.TeacherProfileFragment

class TeacherViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(
        DashboardFragment(),
        TeacherGroupsFragment(),
        NotificationsFragment(),
        TeacherProfileFragment()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}
