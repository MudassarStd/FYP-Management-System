package com.android.cuifypmanagementsystem.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Batch(
    var firestoreId : String?,
    val name : String,
    val semester : Int?,
    val registeredStudents : Int,
    val registeredGroups : Int,
    val fypActivityStatus : Boolean?
) : Serializable{
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    constructor() :this(null, "",null,0,0,null)
}


