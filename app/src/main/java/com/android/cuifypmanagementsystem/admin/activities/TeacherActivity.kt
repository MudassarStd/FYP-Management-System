package com.android.cuifypmanagementsystem.admin.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityTeacherBinding
import com.android.cuifypmanagementsystem.admin.viewmodel.TeacherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TeacherActivity : AppCompatActivity() {
    private val binding : ActivityTeacherBinding by lazy {
        ActivityTeacherBinding.inflate(layoutInflater)
    }

    private val teacherViewModel : TeacherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#576AE0")
        binding.toolbarTeacherActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.cvTeacherList.setOnClickListener {
            startActivity(Intent(this, ManageTeacherActivity::class.java))
        }

        binding.cvRegisterTeacher.setOnClickListener {
            startActivity(Intent(this, AddTeacher::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            binding.tvTotalRegisteredTeachers.text= teacherViewModel.getTotalRegisteredTeacherCount().toString()
            showCountView()
        }
    }

    private fun showCountView() {
        binding.tvTotalRegisteredTeachers.visibility = View.VISIBLE
        binding.progressbarTotalRegisteredTeachers.visibility = View.GONE
    }
}