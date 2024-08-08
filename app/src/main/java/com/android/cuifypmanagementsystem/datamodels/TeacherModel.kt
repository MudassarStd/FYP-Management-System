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
    val isSupervisor : Boolean,
    val isFypHeadOrSecretory : Boolean,
    @Embedded val fypActivityRole: FypActivityRole? = null,
    val registrationTimeStamp: Timestamp = Timestamp.now()
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor() : this(null, "", "", "", false, false ,null, Timestamp.now()) // No-argument constructor
}
