package com.android.cuifypmanagementsystem.datamodels

import androidx.room.Entity
import java.io.Serializable

data class TeacherNameAndId(
    val name : String = "",
    val firestoreId : String = ""
)

data class BatchInfo(
    val name : String = "",
    val batchId : String = ""
)

@Entity
data class FypActivityRecord(
    val firestoreId: String?,
    val fypHeadId: String?,
    val fypSecId : String?,
    val batchId: String?,
    val status : Boolean = false,
    val registrationTimeStamp : Long
) : Serializable {
    constructor() : this(null,null, null, null, false, 0L) // Empty constructor
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