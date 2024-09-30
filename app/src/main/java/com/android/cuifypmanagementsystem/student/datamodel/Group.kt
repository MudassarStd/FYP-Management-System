package com.android.cuifypmanagementsystem.student.datamodel

data class Group(
    val firestoreId : String?,
    val groupMembers : List<String>?,
    val membersCount : Int?,
    val batch : String?,
    val project : Project?,
    val supervisor : String?,
    val registrationDate : Long
) {
    constructor() : this(null, null, null, null, null, null, 0L)
}
