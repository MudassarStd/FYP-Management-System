package com.android.cuifypmanagementsystem.datamodels

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.cuifypmanagementsystem.student.datamodel.Project
import com.google.firebase.Timestamp
import java.io.Serializable

data class FypActivityRole(
    val activityId : String = "",
    val activityRole : String = ""
)

data class GroupRequest(
    val batch : String = "",
    var requests : List<String>?
){
    constructor() : this(batch = "", requests = null)
}

data class GroupData(
    val batch : String = "",
    var groups : List<String>?
)
{
    constructor() : this(batch = "", groups = null)
}

@Entity
data class Teacher(
    var firestoreId: String? = null,
    val name: String,
    val profileImage : String?,
    val email: String,
    val department: String,
    val supervisor: Int,
    var groupRequests: List<GroupRequest>? = null,
    var groups : List<GroupData>? = null,
    val fypHeadOrSecretory: Int,
    @Embedded val fypActivityRole: FypActivityRole? = null,
    val registrationTimeStamp: Long
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor() : this(null, "", null ,"", "", 0,null, null, 0, null, 0L)
}
