package com.android.cuifypmanagementsystem.apiservice

// Define the data models
data class Recipient(val email: String, val name: String? = null)

data class Sender(val email: String, val name: String? = null)

data class MailerSendEmail(
    val to: List<Recipient>,
    val from: Sender,
    val subject: String,
    val text: String,
    val html: String
)

data class MailerSendResponse(val messageId: String)
