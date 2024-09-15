package com.android.cuifypmanagementsystem.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTime {

    fun longToDate(input : Long, patten : String = "dd MMMM, yyyy") : String {
        val date = Date(input)
        val dateFormat = SimpleDateFormat(patten, Locale.getDefault())
        return dateFormat.format(date)
    }
}