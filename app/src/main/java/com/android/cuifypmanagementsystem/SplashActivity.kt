package com.android.cuifypmanagementsystem

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.utils.UserAuthNavigationManager
import com.android.cuifypmanagementsystem.utils.UserAuthSharedPreferenceHelper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userAuthData = UserAuthSharedPreferenceHelper.getUserAuthData()

        if(userAuthData != null){
            UserAuthNavigationManager.navigateToAppropriateScreen(this, userAuthData)
        }
        else{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish()
    }
}