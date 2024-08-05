package com.android.cuifypmanagementsystem

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.databinding.ActivityLoginBinding
import com.android.cuifypmanagementsystem.datamodels.LoginCredentials
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
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

        binding.btnLogin.setOnClickListener {
            val login = getLoginCredentials()

            login?.let {
                loginUser(login.email, login.password)

            } ?: Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLoginCredentials(): LoginCredentials? {
        val email = binding.etEmailLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()

        return LoginCredentials(email, password).takeIf { email.isNotEmpty() && password.isNotEmpty() }
    }

    private fun loginUser(email: String, password: String) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Success: ${email} & ${password}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Fail: ${email} & ${password}", Toast.LENGTH_SHORT).show()
                    val exception = task.exception
                    // ... display error message or take appropriate action
                }
            }
    }
}