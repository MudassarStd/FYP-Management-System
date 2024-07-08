package com.android.cuifypmanagementsystem.room.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Teacher(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val name : String,
    val depart : String,
    val role : String,
) : Serializable
