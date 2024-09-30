package com.android.cuifypmanagementsystem.teacher.datamodel

import com.android.cuifypmanagementsystem.student.datamodel.Project

data class GroupDisplayModel(
    val firestoreId: String? = null,
    val groupMembers : List<String>?,
    val batch : String?,
    val project: Project?,
)
{
    constructor() :  this(null, null, null, null)
}