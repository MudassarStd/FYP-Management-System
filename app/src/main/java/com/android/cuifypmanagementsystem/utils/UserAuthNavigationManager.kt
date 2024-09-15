package com.android.cuifypmanagementsystem.utils

import android.content.Context
import android.content.Intent
import com.android.cuifypmanagementsystem.admin.activities.AdminDashboardActivity
import com.android.cuifypmanagementsystem.datamodels.LoggedInUserData
import com.android.cuifypmanagementsystem.teacher.activities.MainTeacherActivity

object UserAuthNavigationManager {

    fun navigateToAppropriateScreen(context: Context, user : LoggedInUserData) {
        val intent = when (user.role) {
            "admin" -> Intent(context, AdminDashboardActivity::class.java)
            "teacher" -> Intent(context, MainTeacherActivity::class.java)
//            "student" -> Intent(context, StudentActivity::class.java)
            else -> throw IllegalArgumentException("Unknown role: ${user.role}")
        }
        intent.putExtra("userId", user.userId)
        context.startActivity(intent)
    }
}