package com.android.cuifypmanagementsystem.datamodels

import androidx.room.Entity

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
    val fypHead: TeacherNameAndId = TeacherNameAndId(),
    val fypSec: TeacherNameAndId = TeacherNameAndId(),
    val batch: BatchInfo? = null,
    val status : Boolean = false,
    val registrationTimeStamp : Long
)  {
    constructor() : this(TeacherNameAndId("", ""), TeacherNameAndId("", ""), null, false, 0L) // Empty constructor
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