package com.android.cuifypmanagementsystem.room.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Teacher(
    val firestoreId : String?,
    val name : String,
    val email : String,
    val depart : String,
    val role : String,
    val registrationTimeStamp : Long
) : Serializable
{
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}
