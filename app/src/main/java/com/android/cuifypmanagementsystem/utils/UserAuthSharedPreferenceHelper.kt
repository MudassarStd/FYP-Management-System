package com.android.cuifypmanagementsystem.utils

import android.content.Context
import android.content.SharedPreferences
import com.android.cuifypmanagementsystem.datamodels.LoggedInUserData

object UserAuthSharedPreferenceHelper {
    private const val PREFS_NAME = "user_auth_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_ROLE = "user_role"

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserAuthData(userAuthData: LoggedInUserData) {
        with(sharedPreferences.edit()) {
            putString(KEY_USER_ID, userAuthData.userId)
            putString(KEY_USER_ROLE, userAuthData.role)
            apply()
        }
    }

    fun getUserAuthData(): LoggedInUserData? {
        val userId = sharedPreferences.getString(KEY_USER_ID, null)
        val role = sharedPreferences.getString(KEY_USER_ROLE, null)
        return if (userId != null && role != null) {
            LoggedInUserData(userId, role)
        } else {
            null
        }
    }

    fun clearUserAuthData() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}