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

data class FypActivityRecordUiModel(
    val fypActivityRecord : FypActivityRecord?,
    val fypHeadName : String?,
    val fypSecretoryName : String?,
    val batchName : String?
)