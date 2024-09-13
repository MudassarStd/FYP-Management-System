package com.android.cuifypmanagementsystem.student

data class Group(
    val firestoreId : String,
    val groupMembers : List<String>,
    val membersCount : Int
)
