package com.android.cuifypmanagementsystem.student.datamodel

data class Project(
    val title : String,
    val description : String,
    val techStack : List<String>?
) {
    constructor() : this("", "", null)
}
