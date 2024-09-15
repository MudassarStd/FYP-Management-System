package com.android.cuifypmanagementsystem

import CustomDialogHelper.showActionSuccessDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.graphics.Color
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.android.cuifypmanagementsystem.databinding.ActivityForgetPasswordBinding
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.auth.viewmodel.UserAuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgetPasswordActivity : AppCompatActivity() {

    private val binding: ActivityForgetPasswordBinding by lazy {
        ActivityForgetPasswordBinding.inflate(layoutInflater)
    }
    private val userAuthViewModel: UserAuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupEdgeToEdgePadding()

        window.statusBarColor = Color.parseColor("#6173E3")

        // Initialize ViewModel
//        val userAuthRepository = (application as BaseApplication).userAuthRepository
//        userAuthViewModel = ViewModelProvider(
//            this,
//            UserAuthViewModelFactory(userAuthRepository)
//        )[UserAuthViewModel::class.java]

        // Set up button click listener for password reset
        binding.btnResetPassword.setOnClickListener {
            handlePasswordReset()
        }

        // Set up navigation click listener
        binding.toolbarForgetPassword.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupEdgeToEdgePadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handlePasswordReset() {
        val email = binding.etEmailForgetPassword.text.toString().trim()


        if (email.isNotEmpty() && isValidEmail(email)) {

            hideKeyboard()
            binding.etEmailForgetPassword.clearFocus()

            userAuthViewModel.sendPasswordResetEmail(email)
            userAuthViewModel.passwordResetEmailState.observe(this){result ->
                when(result){
                    is Result.Success -> {
                        hideProgressDialog()
                        showActionSuccessDialog(this, "A password reset link has been sent to your email")
                    }
                    is Result.Failure -> {
                        hideProgressDialog()
                        Toast.makeText(this, "Failed to send password reset link", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        showProgressDialog("Processing request...", this)
                    }
                }
            }
        } else {
            // Optionally, show an error message if the email is invalid
            binding.etEmailForgetPassword.error = "Please enter a valid email address"
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etEmailForgetPassword.windowToken, 0)
    }
}
