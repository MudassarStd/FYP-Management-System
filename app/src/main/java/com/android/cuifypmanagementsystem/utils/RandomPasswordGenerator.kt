package com.android.cuifypmanagementsystem.utils

object RandomPasswordGenerator {

    fun generateRandomPassword(length: Int = 8): String {
        require(length in 8..12) { "Password length must be between 8 and 12 characters." }

        val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" // Digits
        return (1..length)
            .map { chars.random() } // Pick random characters from the set
            .joinToString("") // Join them into a single string
    }
}