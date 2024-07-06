package com.android.cuifypmanagementsystem.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Batch(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val name : String,
    val semester : Int,
    val registeredStudents : Int
)

@Entity
data class Admin(
        @PrimaryKey(autoGenerate = true)
        val id : Int,
        val email : String,
        val pass : String
        )
