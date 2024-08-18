package com.android.cuifypmanagementsystem.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

data class LoginCredentials (
        val email : String,
        val password : String
        )


@Entity
data class Admin(
        @PrimaryKey(autoGenerate = true)
        val id : Int,
        val email : String,
        val pass : String
)