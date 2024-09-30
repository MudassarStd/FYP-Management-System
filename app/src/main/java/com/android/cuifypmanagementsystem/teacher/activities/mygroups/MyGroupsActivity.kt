package com.android.cuifypmanagementsystem.teacher.activities.mygroups

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityMyGroupsBinding
import com.android.cuifypmanagementsystem.teacher.adapter.pager.TeacherGroupsAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyGroupsActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMyGroupsBinding.inflate(layoutInflater)}
    private val myGroupViewModel : MyGroupsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#576AE0")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // getting teacher Id
        val firebaseAuth = FirebaseAuth.getInstance()
        val teacherId = firebaseAuth.currentUser?.uid
        teacherId?.let {
            myGroupViewModel.setTeacherId(it)
        }

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
    }
}