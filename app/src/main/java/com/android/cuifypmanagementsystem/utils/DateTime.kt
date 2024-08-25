package com.android.cuifypmanagementsystem.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTime {

    fun longToDate(input : Long, ) : String {
        val date = Date(input)
        val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }
}