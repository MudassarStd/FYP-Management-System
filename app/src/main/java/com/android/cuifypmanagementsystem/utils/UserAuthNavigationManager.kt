package com.android.cuifypmanagementsystem.utils

import android.content.Context
import android.content.Intent
import com.android.cuifypmanagementsystem.admin.AdminDashboardActivity
import com.android.cuifypmanagementsystem.admin.ManageTeacherActivity
import com.android.cuifypmanagementsystem.datamodels.LoggedInUserData

object UserAuthNavigationManager {

    fun navigateToAppropriateScreen(context: Context, user : LoggedInUserData) {
        val intent = when (user.role) {
            "admin" -> Intent(context, AdminDashboardActivity::class.java)
            "teacher" -> Intent(context, ManageTeacherActivity::class.java)
//            "student" -> Intent(context, StudentActivity::class.java)
            else -> throw IllegalArgumentException("Unknown role: ${user.role}")
        }
        intent.putExtra("UserId", user.userId)
        context.startActivity(intent)
    }
}