package com.android.cuifypmanagementsystem.student


data class Student(
    val firestoreId : String?,
    val name : String,
    val email : String,
    val registrationNumber : String,
    val semester : Int,
    val depart : String,
    val group : Group?,
    val registrationDate : Long
)
