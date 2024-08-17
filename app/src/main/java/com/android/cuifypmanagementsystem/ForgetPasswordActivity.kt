package com.android.cuifypmanagementsystem

import CustomDialogHelper.showActionSuccessDialog
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.databinding.ActivityForgetPasswordBinding
import com.android.cuifypmanagementsystem.viewmodel.UserAuthViewModel
import com.android.cuifypmanagementsystem.viewmodel.UserAuthViewModelFactory

class ForgetPasswordActivity : AppCompatActivity() {

    private val binding : ActivityForgetPasswordBinding by lazy{
        ActivityForgetPasswordBinding.inflate(layoutInflater)
    }

    private lateinit var userAuthViewModel: UserAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userAuthRepository = (application as BaseApplication).userAuthRepository
        userAuthViewModel = ViewModelProvider(this, UserAuthViewModelFactory(userAuthRepository))[UserAuthViewModel::class.java]

        binding.btnResetPassword.setOnClickListener {
            val email = binding.etEmailForgetPassword.text.toString()

            if (email.isNotEmpty()) {
                userAuthViewModel.sendPasswordResetEmail(email)
                showActionSuccessDialog(this, "An email has been sent to reset password")
                binding.etEmailForgetPassword.setText("")
            }

        }

        binding.toolbarForgetPassword.setNavigationOnClickListener {
            onBackPressed()
        }

    }

}