package com.android.cuifypmanagementsystem.teacher.adapter.recyclerview.callbacks

interface OnGroupRequestClick {
    fun onApproveClick(groupId: String, batch: String?)
    fun onRejectClick(groupId : String)
}