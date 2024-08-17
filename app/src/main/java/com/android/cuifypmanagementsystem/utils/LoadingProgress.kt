package com.android.cuifypmanagementsystem.utils

import android.app.ProgressDialog
import android.content.Context

object LoadingProgress {

    private lateinit var progressDialog : ProgressDialog

    fun showProgressDialog(message: String, context : Context): ProgressDialog {
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(true) // Prevent dismissal by back button
        progressDialog.show()
        return progressDialog
    }

    fun hideProgressDialog() {
        progressDialog.dismiss()
    }
}