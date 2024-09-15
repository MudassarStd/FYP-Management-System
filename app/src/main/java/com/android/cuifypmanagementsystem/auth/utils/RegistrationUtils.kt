package com.android.cuifypmanagementsystem.auth.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Month
import java.time.LocalDate

object RegistrationUtils {

    fun validateRegistrationBatch(input: String): Boolean {
        val validPrefixes = listOf("fa", "sp")
        if (input.length != 4) return false
        val prefix = input.substring(0, 2).lowercase()
        val year = input.substring(2)
        if (!validPrefixes.contains(prefix)) return false
        return year.toIntOrNull() != null
    }



    // requires min API  26
    @RequiresApi(Build.VERSION_CODES.O)
    fun getSemesterNumber(regInfo: String): Int {
        // Get the current date
        val currentDate = LocalDate.now()

        // Parsing the registration information
        val semester = regInfo.substring(0, 2)
        val yearSuffix = regInfo.substring(2)
        val regYear = 2000 + yearSuffix.toInt() // Convert to full year (e.g., '21' -> 2021)

        // Determine the current year and semester
        val currentYear = currentDate.year
        val currentMonth = currentDate.month

        val currentSemester = if (currentMonth in Month.JANUARY..Month.JUNE) "sp" else "fa"

        // Calculate the total number of semesters passed
        var semestersPassed = (currentYear - regYear) * 2 // 2 semesters per year

        // Adjustment based on the starting and ending semesters
        semestersPassed += when (currentSemester) {
            "sp" -> if (semester == "fa") 1 else 0
            "fa" -> if (semester == "sp") 2 else 1
            else -> 0
        }

        // Return the total number of semesters passed
        return semestersPassed
    }

    fun splitRegistrationNumber(regNumber: String): Pair<String, String>? {

        val parts = regNumber.split("-")
        return if (parts.size == 2) {
            Pair(parts[0], parts[1])
        } else {
            null
        }
    }

    fun getDepartmentName(departmentCode: String): String {
        return when (departmentCode.lowercase()) {
            "bcs" -> "Computer Science"
            "bse" -> "Software Engineering"
            else -> "Invalid Registration"
        }
    }
}


