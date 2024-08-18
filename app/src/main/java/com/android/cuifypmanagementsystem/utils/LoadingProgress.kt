package com.android.cuifypmanagementsystem.utils

import android.app.ProgressDialog
import android.content.Context

object LoadingProgress {

    private var progressDialog: ProgressDialog? = null

    fun showProgressDialog(message: String, context: Context) {
        // Dismiss any existing dialog before creating a new one
        hideProgressDialog()

        progressDialog = ProgressDialog(context).apply {
            setMessage(message)
            setCancelable(true) // Prevent dismissal by back button
            show()
        }
    }

    fun hideProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            progressDialog = null
        }
    }
}
