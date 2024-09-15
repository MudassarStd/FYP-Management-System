package com.android.cuifypmanagementsystem.auth.utils

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(Regex(emailPattern))
    }

    fun isValidRegistrationNumber(regNumber: String): Boolean {
        val regNumberPattern = "^(fa|sp)\\d{2}-[a-zA-Z]{3}-\\d{3}$"
        return regNumber.matches(Regex(regNumberPattern))
    }

    fun isValidRegistrationNumberForDynamicity(regNumber: String): Boolean {
        val regNumberPattern = "^(fa|sp)\\d{2}-[a-zA-Z]{3}$"
        return regNumber.matches(Regex(regNumberPattern))
    }
}