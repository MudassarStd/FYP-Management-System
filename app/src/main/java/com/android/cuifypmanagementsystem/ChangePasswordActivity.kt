package com.android.cuifypmanagementsystem

import android.content.Context
import android.graphics.Color
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
        window.statusBarColor = Color.parseColor("#576AE0")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toolbarChangePassword.setNavigationOnClickListener {
            onBackPressed()
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

        binding.cbShowPasswords.setOnCheckedChangeListener { _, isChecked ->
            togglePasswordVisibility(isChecked)
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

    private fun togglePasswordVisibility(isVisible: Boolean) {
        val passwordInputType = if (isVisible) {
            // Show password
            android.text.InputType.TYPE_CLASS_TEXT
        } else {
            // Hide password
            android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        binding.etOldPasswordForChange.inputType = passwordInputType
        binding.etNewPasswordForChange.inputType = passwordInputType
        binding.etConfirmNewPasswordForChange.inputType = passwordInputType

        // Move the cursor to the end of the text
        binding.etOldPasswordForChange.setSelection(binding.etOldPasswordForChange.text?.length ?: 0)
        binding.etNewPasswordForChange.setSelection(binding.etNewPasswordForChange.text?.length ?: 0)
        binding.etConfirmNewPasswordForChange.setSelection(binding.etConfirmNewPasswordForChange.text?.length ?: 0)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etConfirmNewPasswordForChange.windowToken, 0)
    }
}