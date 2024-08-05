package com.android.cuifypmanagementsystem.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {
    private val binding : ActivityAdminDashboardBinding by lazy {
        ActivityAdminDashboardBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.section3.setOnClickListener {
            startActivity(Intent(this, ManageTeacher::class.java))
        }

        binding.section1.setOnClickListener {
            startActivity(Intent(this, BatchActivity::class.java))
        }
        binding.section2.setOnClickListener {
            startActivity(Intent(this, Start_FYP_Activityy::class.java))
        }
    }
}