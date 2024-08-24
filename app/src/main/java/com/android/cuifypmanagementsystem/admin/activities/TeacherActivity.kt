package com.android.cuifypmanagementsystem.admin.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityTeacherBinding

class TeacherActivity : AppCompatActivity() {
    private val binding : ActivityTeacherBinding by lazy {
        ActivityTeacherBinding.inflate(layoutInflater)
    }
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
}