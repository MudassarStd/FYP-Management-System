package com.android.cuifypmanagementsystem.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.io.Serializable

//@Entity
//data class Teacher(
//    var firestoreId : String? = null,
//    val name : String,
//    val email : String,
//    val depart : String,
//    val role : String,
//    val registrationTimeStamp: Timestamp = Timestamp.now()
//) : Serializable
//{
//    @PrimaryKey(autoGenerate = true)
//    var id : Long = 0
//}

@Entity
data class Teacher(
    var firestoreId: String? = null,
    val name: String,
    val email: String,
    val department: String,
    val role: String,
    val registrationTimeStamp: Timestamp = Timestamp.now()
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor() : this(null, "", "", "", "", Timestamp.now()) // No-argument constructor
}
