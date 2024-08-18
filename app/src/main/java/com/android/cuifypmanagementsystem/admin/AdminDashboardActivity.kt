package com.android.cuifypmanagementsystem.admin

import CustomDialogHelper.showLoadingDialog
import CustomDialogHelper.showLogoutDialog
import android.content.Intent
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
import com.android.cuifypmanagementsystem.viewmodel.UserAuthViewModelFactory

import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.PopupMenu
import com.android.cuifypmanagementsystem.ChangePasswordActivity
import com.android.cuifypmanagementsystem.LoginActivity

class AdminDashboardActivity : AppCompatActivity() {
    private val binding : ActivityAdminDashboardBinding by lazy {
        ActivityAdminDashboardBinding.inflate(layoutInflater)
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


        binding.section3.setOnClickListener {
            startActivity(Intent(this, ManageTeacherActivity::class.java))
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
}