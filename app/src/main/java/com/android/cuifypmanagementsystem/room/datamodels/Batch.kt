package com.android.cuifypmanagementsystem.room.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Batch(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val name : String,
    val semester : Int,
    val registeredStudents : Int
) : Serializable

@Entity
data class Admin(
        @PrimaryKey(autoGenerate = true)
        val id : Int,
        val email : String,
        val pass : String
        )
