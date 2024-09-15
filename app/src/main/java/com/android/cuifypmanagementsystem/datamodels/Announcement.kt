package com.android.cuifypmanagementsystem.datamodels

data class Announcement(
    val id: String?,
    val title: String,
    val description: String,
    val timestamp: Long,
    val author: String,
    val audience : String,
    val priority: Boolean
)
