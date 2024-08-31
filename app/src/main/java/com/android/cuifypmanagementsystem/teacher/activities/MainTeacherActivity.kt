package com.android.cuifypmanagementsystem.teacher.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityMainTeacherBinding
import com.android.cuifypmanagementsystem.teacher.adapter.pager.TeacherViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainTeacherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainTeacherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewPager = binding.teacherMainViewPager
        val bsv = binding.teacherMainBSV

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
