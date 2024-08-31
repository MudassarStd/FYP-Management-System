package com.android.cuifypmanagementsystem.datamodels

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.io.Serializable

data class FypActivityRole(
    val activityId : String = "",
    val activityRole : String = ""
)

@Entity
data class Teacher(
    var firestoreId: String? = null,
    val name: String,
    val email: String,
    val department: String,
    val supervisor: Int ,
    val fypHeadOrSecretory: Int,
    @Embedded val fypActivityRole: FypActivityRole? = null,
    val registrationTimeStamp: Long
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor() : this(null, "", "", "", 0, 0, null, 0L)
}
