package com.android.cuifypmanagementsystem.student.datamodel


data class Student(
    val firestoreId : String?,
    val name : String,
    val email : String,
    val registrationNumber : String,
    val batch : String,
    val semester : Int,
    val depart : String,
    val group : String?,
    val registrationDate : Long
) {
    constructor() : this(null, "", "", "", "",0, "", null, 0)
}
