package com.android.cuifypmanagementsystem.datamodels

data class FypIdea(
    var firestoreId : String?,
    val title : String?,
    val description : String?,
    val links : List<String>?,
    val ideaTaken : Boolean,
    val author : String?,
    val dateTime : Long
) {
    constructor() : this(null, null, null, null,false ,null, 0L)
}

