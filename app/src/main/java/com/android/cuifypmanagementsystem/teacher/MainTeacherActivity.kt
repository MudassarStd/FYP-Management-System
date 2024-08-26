package com.android.cuifypmanagementsystem.teacher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityMainTeacherBinding
import com.android.cuifypmanagementsystem.teacher.adapter.TeacherViewPagerAdapter

class MainTeacherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainTeacherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.teacherViewPager
        val bsv = binding.teacherBSV

        // Set up ViewPager with the adapter
        viewPager.adapter = TeacherViewPagerAdapter(this)

        // Handle BottomNavigationView item clicks
        bsv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> viewPager.currentItem = 0
                R.id.navigation_teacher_groups -> viewPager.currentItem = 1
                R.id.navigation_notifications -> viewPager.currentItem = 2
                R.id.navigation_teacher_profile -> viewPager.currentItem = 3
            }
            true
        }

        // Sync ViewPager with BottomNavigationView
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bsv.menu.getItem(position).isChecked = true
            }
        })
    }
}
