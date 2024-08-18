package com.android.cuifypmanagementsystem.apiservice
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MailerSendService {
    @Headers("Authorization: Bearer mlsn.093c8b2f0c8cff5e449a37b8a354abecb71ecffe6182ec5f2e209396b5bf1476", "Content-Type: application/json")
    @POST("email")
    fun sendEmail(@Body email: MailerSendEmail): Call<MailerSendResponse>
}