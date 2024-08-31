package com.android.cuifypmanagementsystem.admin.activities

import CustomDialogHelper.showLogoutDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.android.cuifypmanagementsystem.BaseApplication
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityAdminDashboardBinding
import com.android.cuifypmanagementsystem.viewmodel.UserAuthViewModel

import android.view.MenuInflater
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import com.android.cuifypmanagementsystem.ChangePasswordActivity
import com.android.cuifypmanagementsystem.LoginActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdminDashboardActivity : AppCompatActivity() {
    private val binding : ActivityAdminDashboardBinding by lazy {
        ActivityAdminDashboardBinding.inflate(layoutInflater)
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

        window.statusBarColor = Color.parseColor("#576AE0")

//        val userAuthRepository = (application as BaseApplication).userAuthRepository
//        userAuthViewModel = ViewModelProvider(this, UserAuthViewModelFactory(userAuthRepository))[UserAuthViewModel::class.java]


        binding.section3.setOnClickListener {
            startActivity(Intent(this, TeacherActivity::class.java))
        }

        binding.section1.setOnClickListener {
            startActivity(Intent(this, BatchActivity::class.java))
        }
        binding.section2.setOnClickListener {
            startActivity(Intent(this, AllFypActivity::class.java))
        }

        binding.btnLogoutAdmin.setOnClickListener {
            showLogoutDialog(
                context = this,
                onLogout = {
                    userAuthViewModel.userLogout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            )
        }

        binding.btnDotsMenuAdminPanel.setOnClickListener {
            showPopUpMenu(it)
//            showLoadingDialog(this)
        }
    }

    private fun showPopUpMenu(view: View) {

        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.admin_dashboard_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            // Handle menu item clicks
            when (item.itemId) {
                R.id.adminDashboardMenuChangePassword -> {
                    startActivity(Intent(this, ChangePasswordActivity::class.java))
                    true
                }

                else -> false
            }
        }

        popupMenu.show()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}