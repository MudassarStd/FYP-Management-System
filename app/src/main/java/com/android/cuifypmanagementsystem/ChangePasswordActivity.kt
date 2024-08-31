package com.android.cuifypmanagementsystem

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.databinding.ActivityChangePasswordBinding
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.viewmodel.UserAuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {
    private val binding : ActivityChangePasswordBinding by lazy {
        ActivityChangePasswordBinding.inflate(layoutInflater)
    }

    private val userAuthViewModel: UserAuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize ViewModel
//        val userAuthRepository = (application as BaseApplication).userAuthRepository
//        userAuthViewModel = ViewModelProvider(
//            this,
//            UserAuthViewModelFactory(userAuthRepository)
//        )[UserAuthViewModel::class.java]



        binding.btnChangePassword.setOnClickListener {
            handleChangePassword()
        }

    }

    private fun handleChangePassword() {
        val oldPassword = binding.etOldPasswordForChange.text.toString().trim()
        val newPassword = binding.etNewPasswordForChange.text.toString().trim()
        val confirmPassword = binding.etConfirmNewPasswordForChange.text.toString().trim()

        // Validate input
        if (newPassword.length < 8) {
            binding.etNewPasswordForChange.error = "Password must be at least 8 characters long"
            return
        }
        if (newPassword != confirmPassword) {
            binding.etConfirmNewPasswordForChange.error = "Passwords do not match"
            return
        }

        userAuthViewModel.userChangePassword(oldPassword, newPassword)
        hideKeyboard()


        userAuthViewModel.passwordChangeResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    hideProgressDialog()
                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity or redirect as needed
                }
                is Result.Failure -> {
                    hideProgressDialog()
                    Toast.makeText(this, "Failed: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showProgressDialog("Updating password..", this)
                }

            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etConfirmNewPasswordForChange.windowToken, 0)
    }



}