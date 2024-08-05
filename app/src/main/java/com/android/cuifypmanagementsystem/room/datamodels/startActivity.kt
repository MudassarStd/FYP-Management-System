package com.android.cuifypmanagementsystem.room.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity
data class startActivity(
    val fypHead: String = "",
    val fypSec: String = "",
    val startYear: String = "",
    val status: String = "",
    val registrationTimeStamp : String
)  {
    constructor() : this("", "", "", "", "") // Empty constructor
}

@Entity
data class DisplayTeacher(
    val firestoreId : String?,
    val name : String,
    val email : String,
    val depart : String,
    val role : String,
    val registrationTimeStamp : Long

)  {
    constructor() : this("", "","","","",0) // Empty constructor
}