package com.android.cuifypmanagementsystem

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.databinding.ActivityAddTeacherBinding
import com.android.cuifypmanagementsystem.room.datamodels.Teacher
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModel
import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModelFactory

class AddTeacher : AppCompatActivity() {
    private val binding : ActivityAddTeacherBinding by lazy {
        ActivityAddTeacherBinding.inflate(layoutInflater)
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

        val teacherRepository = (application as MainApplication).teacherRepository
        val teacherViewModel = ViewModelProvider(this, TeacherViewModelFactory(teacherRepository))[TeacherViewModel::class.java]

        binding.btnAddTeacher.setOnClickListener {
            val teacher = getTeacherData()
            teacher?.let {
                // insertion into room
                teacherViewModel.addTeacher(teacher)

                Log.d("TeacherCRUDTesting", "AddTeacher: teacher added successfully")
                Toast.makeText(this, "Teacher Added Successfully", Toast.LENGTH_SHORT).show()

                clearFields()
            }
        }

        teacherViewModel.teachers.observe(this){
            Log.d("TeacherCRUDTesting", "AddTeacher: teachers fetched: $it")
        }

    }

    private fun clearFields() {
        binding.etTeacherDepart.setText("")
        binding.etTeacherName.setText("")
        binding.etTeacherEmail.setText("")
        binding.etTeacherRole.setText("")
        binding.etTeacherEmail.clearFocus()
    }

    private fun getTeacherData(): Teacher? {
        val name = binding.etTeacherName.text.toString()
        val email = binding.etTeacherEmail.text.toString()
        val depart = binding.etTeacherDepart.text.toString()
        val role = binding.etTeacherRole.text.toString()

        return if(name.isNotEmpty() && email.isNotEmpty())
        {
            Teacher(0, name, depart, role)
        } else {
            null
        }
    }
}