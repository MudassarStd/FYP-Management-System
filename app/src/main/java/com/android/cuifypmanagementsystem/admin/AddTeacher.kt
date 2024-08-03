package com.android.cuifypmanagementsystem.admin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityAddTeacherBinding
import com.android.cuifypmanagementsystem.room.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_EDIT_TEACHER_OBJECT
import com.android.cuifypmanagementsystem.utils.Constants.INTENT_ACTION_EDIT_TEACHER
import com.android.cuifypmanagementsystem.utils.Result
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

        val teacherRepository = (application as BaseApplication).teacherRepository
        val teacherViewModel = ViewModelProvider(this, TeacherViewModelFactory(teacherRepository))[TeacherViewModel::class.java]


        // Edit Teacher Data
        if(intent.action == INTENT_ACTION_EDIT_TEACHER)
        {
            binding.tvTitleAddTeachers.text = "Update Teacher"
            toggleButtons()
            getTeacherFromIntent()
        }

        binding.btnAddTeacher.setOnClickListener {
            val teacher = getTeacherData()
            teacher?.let {
                // insertion into room
                teacherViewModel.registerTeacher(teacher)

                // observing teacher registration results
                teacherViewModel.teacherRegistrationResult.observe(this){ result ->
                   when(result){
                       is Result.Success -> {
                           Toast.makeText(this, "Teacher Registered Successfully", Toast.LENGTH_SHORT).show()
                       }
                       is Result.Failure -> {
                           val errorMessage = result.exception.message ?: "An unknown error occurred"
                           Toast.makeText(this, "Registration failed: $errorMessage", Toast.LENGTH_SHORT).show()
                       }
                   }
                }

                Log.d("TeacherCRUDTesting", "AddTeacher: teacher added successfully")
//                Toast.makeText(this, "Teacher Added Successfully", Toast.LENGTH_SHORT).show()

                clearFields()
            }
        }


        // UPDATED
        binding.btnUpdateTeacher.setOnClickListener {
            val teacher = getTeacherData()
            teacher?.let{
                Toast.makeText(this, teacher.depart, Toast.LENGTH_SHORT).show()
                teacherViewModel.updateTeacher(teacher)
                clearFields()
                finish()
            }
        }

        teacherViewModel.teachers.observe(this){
            Log.d("TeacherCRUDTesting", "AddTeacher: teachers fetched: $it")
        }

    }

    // UPDATED
    private fun toggleButtons() {
        binding.btnAddTeacher.visibility = View.GONE
        binding.btnUpdateTeacher.visibility = View.VISIBLE
    }

    private fun getTeacherFromIntent() {
        val teacher = intent.getSerializableExtra(ACTION_EDIT_TEACHER_OBJECT) as Teacher

        binding.etTeacherName.setText(teacher.name)
        binding.etTeacherDepart.setText(teacher.depart)

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
            Teacher(null, name,email, depart, role)
        } else {
            null
        }
    }
}