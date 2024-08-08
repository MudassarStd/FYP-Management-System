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
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.utils.Constants.ACTION_EDIT_TEACHER_OBJECT
import com.android.cuifypmanagementsystem.utils.Constants.INTENT_ACTION_EDIT_TEACHER
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
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
            Teacher(null, name,email, depart, false, false, null)
        } else {
            null
        }
    }



    // MailerSend API implementation
//    fun sendEmail() {
//        val service = RetrofitClient.instance.create(MailerSendService::class.java)
//
//        val email = MailerSendEmail(
//            to = listOf(Recipient("mudassarstd@gmail.com", "Recipient Name")),
//            from = Sender("mudassarstd@gmail.com", "Your Name"),
//            subject = "Email Subject",
//            text = "This is the text content",
//            html = "<p>This is the HTML content</p>"
//        )
//
//        service.sendEmail(email).enqueue(object : Callback<MailerSendResponse> {
//            override fun onResponse(call: Call<MailerSendResponse>, response: Response<MailerSendResponse>) {
//                if (response.isSuccessful) {
//                    Log.d("TESTINGMAILINGSERVICE", "Email sent successfully, message ID: ${response.body()?.messageId}")
//                } else {
//                    println()
//                    Log.d("TESTINGMAILINGSERVICE", "Failed to send email: ${response.errorBody()?.string()}")
//
//                }
//            }
//
//            override fun onFailure(call: Call<MailerSendResponse>, t: Throwable) {
//                t.printStackTrace()
//                println("Failed to send email: ${t.message}")
//                Log.d("TESTINGMAILINGSERVICE", "Failed to send email: ${t.message}")
//
//            }
//        })
//    }



}