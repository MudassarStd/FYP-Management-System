package com.android.cuifypmanagementsystem.admin.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityAddTeacherBinding
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_EDIT_TEACHER_OBJECT
import com.android.cuifypmanagementsystem.utils.Constants.INTENT_ACTION_EDIT_TEACHER
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.admin.viewmodel.TeacherViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddTeacher : AppCompatActivity() {
    private val binding : ActivityAddTeacherBinding by lazy {
        ActivityAddTeacherBinding.inflate(layoutInflater)
    }
    private val teacherViewModel : TeacherViewModel by viewModels()

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
                hideKeyboard()
                teacherViewModel.registerTeacher(teacher)

                // ---------------------- observing teacher registration results ----------------------

                teacherViewModel.teacherRegistrationResult.observe(this){ result ->
                   when(result){
                       is Result.Loading -> {
                          showProgressDialog("Registering Teacher, please wait..", this)
                       }
                       is Result.Success -> {
                           hideProgressDialog()
                           clearFields()
                           Toast.makeText(this, "Teacher Registered Successfully", Toast.LENGTH_SHORT).show()
                       }
                       is Result.Failure -> {
                           hideProgressDialog()
                           val errorMessage = result.exception.message ?: "An unknown error occurred"
                           Toast.makeText(this, "Registration failed: $errorMessage", Toast.LENGTH_SHORT).show()
                       }
                   }
                }
            }
        }


        // UPDATED
        binding.btnUpdateTeacher.setOnClickListener {
            val teacher = getTeacherData()
            teacher?.let{
                Toast.makeText(this, teacher.department, Toast.LENGTH_SHORT).show()
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
        binding.etTeacherDepart.setText(teacher.department)

    }

    private fun clearFields() {
        binding.etTeacherDepart.setText("")
        binding.etTeacherName.setText("")
        binding.etTeacherEmail.setText("")
        binding.etTeacherEmail.clearFocus()
        binding.etTeacherDepart.clearFocus()
    }

    private fun getTeacherData(): Teacher? {
        val name = binding.etTeacherName.text.toString()
        val email = binding.etTeacherEmail.text.toString()
        val depart = binding.etTeacherDepart.text.toString()

        return if(name.isNotEmpty() && email.isNotEmpty())
        {
            Teacher(null, name, null, email, depart, 0, 0, null, System.currentTimeMillis())
        } else {
            null
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etTeacherDepart.windowToken, 0)
    }
}