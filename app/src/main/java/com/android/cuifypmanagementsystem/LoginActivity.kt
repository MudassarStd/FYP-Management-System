package com.android.cuifypmanagementsystem

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.databinding.ActivityLoginBinding
import com.android.cuifypmanagementsystem.datamodels.LoginCredentials
import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
import com.android.cuifypmanagementsystem.utils.Result
import com.android.cuifypmanagementsystem.utils.UserAuthNavigationManager
import com.android.cuifypmanagementsystem.viewmodel.UserAuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
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

//        val userAuthRepository = (application as BaseApplication).userAuthRepository
//        userAuthViewModel = ViewModelProvider(this, UserAuthViewModelFactory(userAuthRepository))[UserAuthViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val loginCredentials = getLoginCredentials()

            loginCredentials?.let {
                userAuthViewModel.userLogin(loginCredentials.email, loginCredentials.password)
                userAuthViewModel.setRememberMeState(binding.cbRememberMeLogin.isChecked)
                observeUserLoginState()
//                LoadingProgress.showProgressDialog("Logging in, please wait", this)

//                loginUser()

            } ?: Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
        }


        binding.tvForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }
    }

    private fun observeUserLoginState() {
        userAuthViewModel.userAuthState.observe(this){ result ->
            when(result){
                is Result.Success -> {
                    hideProgressDialog()
                    UserAuthNavigationManager.navigateToAppropriateScreen(this, result.data)
                    finish()
                }
                is Result.Failure -> {
                    hideProgressDialog()
//                    Toast.makeText(this, "Login Failed: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()

                }
                is Result.Loading -> {
                    showProgressDialog("Logging in...", this)
                }
            }
        }
    }

    private fun getLoginCredentials(): LoginCredentials? {
        val email = binding.etEmailLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()

        return LoginCredentials(email, password).takeIf { email.isNotEmpty() && password.isNotEmpty() }
    }
}