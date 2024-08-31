package com.android.cuifypmanagementsystem.adapters.pagers

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.cuifypmanagementsystem.admin.fragments.ClosedFypActivitiesFragment
import com.android.cuifypmanagementsystem.admin.fragments.CurrentFypActivitiesFragment


class FypActivityPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentFypActivitiesFragment()
            1 -> ClosedFypActivitiesFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
