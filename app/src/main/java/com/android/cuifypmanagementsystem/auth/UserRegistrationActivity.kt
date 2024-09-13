package com.android.cuifypmanagementsystem.auth

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.auth.model.StudentRegistration
import com.android.cuifypmanagementsystem.auth.utils.RegistrationUtils.getDepartmentName
import com.android.cuifypmanagementsystem.auth.utils.RegistrationUtils.getSemesterNumber
import com.android.cuifypmanagementsystem.auth.utils.RegistrationUtils.splitRegistrationNumber
import com.android.cuifypmanagementsystem.auth.utils.ValidationUtils.isValidEmail
import com.android.cuifypmanagementsystem.auth.utils.ValidationUtils.isValidRegistrationNumber
import com.android.cuifypmanagementsystem.auth.utils.ValidationUtils.isValidRegistrationNumberForDynamicity
import com.android.cuifypmanagementsystem.auth.viewmodel.UserAuthViewModel
import com.android.cuifypmanagementsystem.databinding.ActivityUserRegistrationBinding
import com.android.cuifypmanagementsystem.student.Student
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.utils.UserAuthNavigationManager
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class UserRegistrationActivity : AppCompatActivity() {
    private val binding by lazy {ActivityUserRegistrationBinding.inflate(layoutInflater)}
    private val userAuthViewModel : UserAuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setupWatcherForRegistration()
        binding.btnRegisterUser.setOnClickListener {
            val (studentRegistration , student) = collectDataFromFields()
            if (studentRegistration != null && student != null) {
                userAuthViewModel.registerStudent(studentRegistration, student)
                observeStudentRegistrationState()
            }
        }
    }

    private fun setupWatcherForRegistration() {
        binding.etUserRegistrationNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val regNumber = s.toString().trim()
                if (isValidRegistrationNumberForDynamicity(regNumber)) {
                    val (session, departCode) = splitRegistrationNumber(regNumber)!!
                    val semester = getSemesterNumber(session)
                    val departmentName = getDepartmentName(departCode)

                    binding.etUserRegistrationSemester.setText(semester.toString())
                    binding.etUserRegistrationDepartment.setText(departmentName)
                } else {
                    binding.etUserRegistrationSemester.setText("")
                    binding.etUserRegistrationDepartment.setText("")
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun collectDataFromFields(): Pair<StudentRegistration?, Student?> {
        val name = binding.etUserRegistrationName.text.toString().trim()
        val email = binding.etUserRegistrationEmail.text.toString().trim()
        val regNumber = binding.etUserRegistrationNumber.text.toString().trim()
        val semesterText = binding.etUserRegistrationSemester.text.toString().trim()
        val department = binding.etUserRegistrationDepartment.text.toString().trim()
        val password = binding.etUserRegistrationPassword.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || regNumber.isEmpty() ||
            semesterText.isEmpty() || department.isEmpty() || password.isEmpty()) {
            return Pair(null, null)
        }

        if (!isValidEmail(email)) {
            binding.etUserRegistrationEmail.error = "Invalid email format"
            return Pair(null, null)
        }

        if (!isValidRegistrationNumber(regNumber)) {
            binding.etUserRegistrationNumber.error = "Invalid registration number"
            return Pair(null, null)
        }

        val semester = try {
            semesterText.toInt()
        } catch (e: NumberFormatException) {
            return Pair(null, null)
        }

        val studentRegistration = StudentRegistration(email, password)
        val student = Student(
            firestoreId = null,
            name = name,
            email = email,
            registrationNumber = regNumber,
            semester = semester,
            depart = department,
            group = null,
            registrationDate = System.currentTimeMillis()
        )

        return Pair(studentRegistration, student)
    }

    private fun observeStudentRegistrationState() {
        userAuthViewModel.studentRegistrationState.observe(this){ result ->
            when(result){
                is Result.Success -> {
                    hideProgressDialog()
                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showProgressDialog("Registering...", this)
                }
            }
        }
    }
}